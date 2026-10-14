package com.zao.backend.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zao.backend.api.dto.ActualizarEmpleadoRequest;
import com.zao.backend.api.dto.EmpleadoRespuesta;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.EmpleadoRepository;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.identidad.RolEmpleado;
import com.zao.backend.restaurante.Restaurante;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/** Pruebas unitarias del modulo de empleados (capa de servicio). */
@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleados;

    @InjectMocks
    private EmpleadoService servicio;

    /** Los ids los genera la base de datos; en la prueba se asignan a mano. */
    private <T> T conId(T entidad, long id) {
        ReflectionTestUtils.setField(entidad, "id", id);
        return entidad;
    }

    private Empleado empleado() {
        Restaurante r = conId(new Restaurante("Zao Centro", "900123456", "Calle 1 # 2-3", "Medellin"), 1L);
        return conId(new Empleado(r, "Ana Perez", "ana@zao.com", "HASH",
                RolEmpleado.COCINERO_LINEA, LocalDate.of(2026, 1, 15)), 10L);
    }

    @Test
    void listar_sinRestaurante_devuelveTodosLosEmpleadosActivos() {
        when(empleados.findByActivoTrue()).thenReturn(List.of(empleado()));

        List<EmpleadoRespuesta> lista = servicio.listar(null);

        assertEquals(1, lista.size());
        verify(empleados, never()).findByRestaurante_IdAndActivoTrue(any());
    }

    @Test
    void listar_conRestaurante_filtraPorEseRestaurante() {
        when(empleados.findByRestaurante_IdAndActivoTrue(1L)).thenReturn(List.of(empleado()));

        List<EmpleadoRespuesta> lista = servicio.listar(1L);

        assertEquals(1, lista.size());
        verify(empleados, never()).findByActivoTrue();
    }

    @Test
    void consultar_conEmpleadoActivo_devuelveSusDatos() {
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(empleado()));

        EmpleadoRespuesta respuesta = servicio.consultar(10L);

        assertEquals("ana@zao.com", respuesta.correo());
        assertEquals(RolEmpleado.COCINERO_LINEA, respuesta.rol());
    }

    @Test
    void consultar_conEmpleadoInexistente_lanzaNoEncontrado() {
        when(empleados.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultar(99L));
    }

    @Test
    void actualizar_conDatosValidos_cambiaNombreSinEspaciosYRol() {
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(empleado()));

        EmpleadoRespuesta respuesta = servicio.actualizar(10L,
                new ActualizarEmpleadoRequest("  Ana Maria Perez ", RolEmpleado.CHEF_EJECUTIVO));

        assertEquals("Ana Maria Perez", respuesta.nombreCompleto());
        assertEquals(RolEmpleado.CHEF_EJECUTIVO, respuesta.rol());
    }

    @Test
    void actualizar_conEmpleadoInexistente_lanzaNoEncontrado() {
        when(empleados.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.actualizar(99L,
                new ActualizarEmpleadoRequest("Alguien", RolEmpleado.CAJERO)));
    }

    @Test
    void eliminar_conEmpleadoActivo_loMarcaComoInactivoSinBorrarlo() {
        Empleado e = empleado();
        when(empleados.findByIdAndActivoTrue(10L)).thenReturn(Optional.of(e));

        servicio.eliminar(10L);

        assertFalse(e.isActivo());
        assertNotNull(e.getEliminadoEn());
    }

    @Test
    void eliminar_conEmpleadoInexistente_lanzaNoEncontrado() {
        when(empleados.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.eliminar(99L));
    }
}
