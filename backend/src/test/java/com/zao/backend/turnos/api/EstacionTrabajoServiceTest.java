package com.zao.backend.turnos.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Pruebas unitarias del modulo de estaciones de trabajo (capa de servicio). */
@ExtendWith(MockitoExtension.class)
class EstacionTrabajoServiceTest {

    @Mock
    private EstacionTrabajoRepository repositorio;

    @InjectMocks
    private EstacionTrabajoService servicio;

    private EstacionTrabajo estacion(String nombre, String descripcion) {
        EstacionTrabajo e = new EstacionTrabajo();
        e.setNombre(nombre);
        e.setDescripcion(descripcion);
        return e;
    }

    @Test
    void listarTodas_devuelveLoQueEntregaElRepositorio() {
        when(repositorio.findAll()).thenReturn(List.of(estacion("Parrilla", "Cocina caliente")));

        List<EstacionTrabajo> resultado = servicio.listarTodas();

        assertEquals(1, resultado.size());
        assertEquals("Parrilla", resultado.get(0).getNombre());
    }

    @Test
    void crear_conNombreValido_guardaYDevuelveLaEstacion() {
        EstacionTrabajo nueva = estacion("Pasteleria", "Postres");
        when(repositorio.save(nueva)).thenReturn(nueva);

        EstacionTrabajo resultado = servicio.crear(nueva);

        assertEquals("Pasteleria", resultado.getNombre());
        verify(repositorio).save(nueva);
    }

    @Test
    void crear_conNombreNulo_lanzaExcepcionYNoGuarda() {
        EstacionTrabajo sinNombre = estacion(null, "Sin nombre");

        assertThrows(IllegalArgumentException.class, () -> servicio.crear(sinNombre));

        verify(repositorio, never()).save(any());
    }

    @Test
    void crear_conNombreEnBlanco_lanzaExcepcionYNoGuarda() {
        EstacionTrabajo enBlanco = estacion("   ", "Solo espacios");

        assertThrows(IllegalArgumentException.class, () -> servicio.crear(enBlanco));

        verify(repositorio, never()).save(any());
    }

    @Test
    void actualizar_cuandoExiste_cambiaNombreYDescripcion() {
        EstacionTrabajo existente = estacion("Fria", "Entradas");
        when(repositorio.findById(1L)).thenReturn(Optional.of(existente));
        when(repositorio.save(existente)).thenReturn(existente);

        Optional<EstacionTrabajo> resultado = servicio.actualizar(1L, estacion("Garde manger", "Entradas frias"));

        assertTrue(resultado.isPresent());
        assertEquals("Garde manger", resultado.get().getNombre());
        assertEquals("Entradas frias", resultado.get().getDescripcion());
    }

    @Test
    void actualizar_cuandoNoExiste_devuelveVacioYNoGuarda() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        Optional<EstacionTrabajo> resultado = servicio.actualizar(99L, estacion("X", "Y"));

        assertTrue(resultado.isEmpty());
        verify(repositorio, never()).save(any());
    }

    @Test
    void eliminar_cuandoExiste_borraYDevuelveTrue() {
        when(repositorio.existsById(1L)).thenReturn(true);

        assertTrue(servicio.eliminar(1L));

        verify(repositorio).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_devuelveFalseYNoBorra() {
        when(repositorio.existsById(99L)).thenReturn(false);

        assertFalse(servicio.eliminar(99L));

        verify(repositorio, never()).deleteById(any());
    }
}
