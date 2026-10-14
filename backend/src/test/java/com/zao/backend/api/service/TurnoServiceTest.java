package com.zao.backend.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.zao.backend.api.dto.ActualizarTurnoRequest;
import com.zao.backend.api.dto.CrearTurnoRequest;
import com.zao.backend.api.dto.TurnoRespuesta;
import com.zao.backend.api.error.ConflictoException;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.ChecklistRepository;
import com.zao.backend.api.repository.EmpleadoRepository;
import com.zao.backend.api.repository.EstacionRepository;
import com.zao.backend.api.repository.TurnoRepository;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.identidad.RolEmpleado;
import com.zao.backend.restaurante.Restaurante;
import com.zao.backend.tareas.Checklist;
import com.zao.backend.turnos.EstadoTurno;
import com.zao.backend.turnos.Estacion;
import com.zao.backend.turnos.Turno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/** Pruebas unitarias del modulo de turnos (capa de servicio). */
@ExtendWith(MockitoExtension.class)
class TurnoServiceTest {

    @Mock
    private TurnoRepository turnos;

    @Mock
    private EmpleadoRepository empleados;

    @Mock
    private EstacionRepository estaciones;

    @Mock
    private ChecklistRepository checklists;

    @InjectMocks
    private TurnoService servicio;

    /** Los ids los genera la base de datos; en la prueba se asignan a mano. */
    private <T> T conId(T entidad, long id) {
        ReflectionTestUtils.setField(entidad, "id", id);
        return entidad;
    }

    private Restaurante restaurante(long id) {
        return conId(new Restaurante("Zao Centro", "900123456", "Calle 1 # 2-3", "Medellin"), id);
    }

    private Empleado empleado(Restaurante r, long id) {
        return conId(new Empleado(r, "Ana Perez", "ana@zao.com", "HASH",
                RolEmpleado.COCINERO_LINEA, LocalDate.of(2026, 1, 15)), id);
    }

    private Estacion estacion(Restaurante r, long id) {
        return conId(new Estacion(r, "Parrilla"), id);
    }

    private Turno turno(Restaurante r, Empleado e, Estacion s, long id) {
        return conId(new Turno(r, e, s, LocalDate.of(2026, 10, 20),
                LocalTime.of(8, 0), LocalTime.of(16, 0), false, false), id);
    }

    private CrearTurnoRequest solicitudCrear() {
        return new CrearTurnoRequest(10L, 5L, LocalDate.of(2026, 10, 20),
                LocalTime.of(8, 0), LocalTime.of(16, 0), false, false);
    }

    @Test
    void crear_conDatosValidos_guardaElTurnoYSuChecklist() {
        Restaurante r = restaurante(1L);
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(empleado(r, 10L)));
        when(estaciones.findByIdAndActivoTrue(5L)).thenReturn(Optional.of(estacion(r, 5L)));
        when(turnos.save(any(Turno.class))).thenAnswer(inv -> inv.getArgument(0));

        TurnoRespuesta respuesta = servicio.crear(solicitudCrear());

        assertEquals(1L, respuesta.restauranteId());
        assertEquals(10L, respuesta.empleadoId());
        assertEquals(5L, respuesta.estacionId());
        assertEquals(EstadoTurno.PROGRAMADO, respuesta.estado());
        verify(checklists).save(any(Checklist.class));
    }

    @Test
    void crear_conEmpleadoInexistente_lanzaNoEncontradoYNoGuarda() {
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.crear(solicitudCrear()));

        verify(turnos, never()).save(any());
        verifyNoInteractions(estaciones, checklists);
    }

    @Test
    void crear_conEstacionInexistente_lanzaNoEncontradoYNoGuarda() {
        Restaurante r = restaurante(1L);
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(empleado(r, 10L)));
        when(estaciones.findByIdAndActivoTrue(5L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.crear(solicitudCrear()));

        verify(turnos, never()).save(any());
        verify(checklists, never()).save(any());
    }

    @Test
    void crear_conEstacionDeOtroRestaurante_lanzaConflictoYNoGuarda() {
        Restaurante r1 = restaurante(1L);
        Restaurante r2 = restaurante(2L);
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(empleado(r1, 10L)));
        when(estaciones.findByIdAndActivoTrue(5L)).thenReturn(Optional.of(estacion(r2, 5L)));

        assertThrows(ConflictoException.class, () -> servicio.crear(solicitudCrear()));

        verify(turnos, never()).save(any());
        verify(checklists, never()).save(any());
    }

    @Test
    void consultar_conTurnoActivo_devuelveElTurno() {
        Restaurante r = restaurante(1L);
        Turno t = turno(r, empleado(r, 10L), estacion(r, 5L), 7L);
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));

        TurnoRespuesta respuesta = servicio.consultar(7L);

        assertEquals(7L, respuesta.id());
        assertEquals(10L, respuesta.empleadoId());
        assertEquals(LocalTime.of(8, 0), respuesta.horaInicio());
    }

    @Test
    void consultar_conTurnoInexistente_lanzaNoEncontrado() {
        when(turnos.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultar(99L));
    }

    @Test
    void listar_sinRestaurante_devuelveTodosLosTurnosActivos() {
        Restaurante r = restaurante(1L);
        Turno t = turno(r, empleado(r, 10L), estacion(r, 5L), 7L);
        when(turnos.findByActivoTrue()).thenReturn(List.of(t));

        List<TurnoRespuesta> lista = servicio.listar(null);

        assertEquals(1, lista.size());
        verify(turnos, never()).findByRestaurante_IdAndActivoTrue(any());
    }

    @Test
    void listar_conRestaurante_filtraPorEseRestaurante() {
        Restaurante r = restaurante(1L);
        Turno t = turno(r, empleado(r, 10L), estacion(r, 5L), 7L);
        when(turnos.findByRestaurante_IdAndActivoTrue(1L)).thenReturn(List.of(t));

        List<TurnoRespuesta> lista = servicio.listar(1L);

        assertEquals(1, lista.size());
        verify(turnos, never()).findByActivoTrue();
    }

    @Test
    void actualizar_conDatosValidos_cambiaEstacionFechaYHorario() {
        Restaurante r = restaurante(1L);
        Turno t = turno(r, empleado(r, 10L), estacion(r, 5L), 7L);
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));
        when(estaciones.findByIdAndActivoTrue(6L)).thenReturn(Optional.of(estacion(r, 6L)));

        TurnoRespuesta respuesta = servicio.actualizar(7L, new ActualizarTurnoRequest(
                6L, LocalDate.of(2026, 10, 21), LocalTime.of(9, 0), LocalTime.of(17, 0), true, false));

        assertEquals(6L, respuesta.estacionId());
        assertEquals(LocalDate.of(2026, 10, 21), respuesta.fechaTurno());
        assertEquals(LocalTime.of(9, 0), respuesta.horaInicio());
        assertTrue(respuesta.esNocturno());
    }

    @Test
    void actualizar_conEstacionDeOtroRestaurante_lanzaConflictoYNoCambiaElTurno() {
        Restaurante r1 = restaurante(1L);
        Restaurante r2 = restaurante(2L);
        Turno t = turno(r1, empleado(r1, 10L), estacion(r1, 5L), 7L);
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));
        when(estaciones.findByIdAndActivoTrue(6L)).thenReturn(Optional.of(estacion(r2, 6L)));

        assertThrows(ConflictoException.class, () -> servicio.actualizar(7L, new ActualizarTurnoRequest(
                6L, LocalDate.of(2026, 10, 21), LocalTime.of(9, 0), LocalTime.of(17, 0), true, false)));

        assertEquals(LocalTime.of(8, 0), t.getHoraInicio());
    }

    @Test
    void eliminar_conTurnoActivo_loMarcaComoInactivoSinBorrarlo() {
        Restaurante r = restaurante(1L);
        Turno t = turno(r, empleado(r, 10L), estacion(r, 5L), 7L);
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));

        servicio.eliminar(7L);

        assertFalse(t.isActivo());
        assertNotNull(t.getEliminadoEn());
    }

    @Test
    void eliminar_conTurnoInexistente_lanzaNoEncontrado() {
        when(turnos.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.eliminar(99L));
    }
}
