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

import com.zao.backend.api.dto.ChecklistRespuesta;
import com.zao.backend.api.dto.CrearItemRequest;
import com.zao.backend.api.dto.ItemChecklistRespuesta;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.ChecklistRepository;
import com.zao.backend.api.repository.ItemChecklistRepository;
import com.zao.backend.api.repository.TurnoRepository;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.identidad.RolEmpleado;
import com.zao.backend.restaurante.Restaurante;
import com.zao.backend.tareas.Checklist;
import com.zao.backend.tareas.ItemChecklist;
import com.zao.backend.turnos.Estacion;
import com.zao.backend.turnos.Turno;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

/** Pruebas unitarias del modulo de checklist de un turno (capa de servicio). */
@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {

    @Mock
    private TurnoRepository turnos;

    @Mock
    private ChecklistRepository checklists;

    @Mock
    private ItemChecklistRepository items;

    @InjectMocks
    private ChecklistService servicio;

    /** Los ids los genera la base de datos; en la prueba se asignan a mano. */
    private <T> T conId(T entidad, long id) {
        ReflectionTestUtils.setField(entidad, "id", id);
        return entidad;
    }

    private Turno turno() {
        Restaurante r = conId(new Restaurante("Zao Centro", "900123456", "Calle 1 # 2-3", "Medellin"), 1L);
        Empleado e = conId(new Empleado(r, "Ana Perez", "ana@zao.com", "HASH",
                RolEmpleado.COCINERO_LINEA, LocalDate.of(2026, 1, 15)), 10L);
        Estacion s = conId(new Estacion(r, "Parrilla"), 5L);
        return conId(new Turno(r, e, s, LocalDate.of(2026, 10, 20),
                LocalTime.of(8, 0), LocalTime.of(16, 0), false, false), 7L);
    }

    private Checklist checklist(Turno t) {
        return conId(new Checklist(t), 20L);
    }

    private ItemChecklist item(Checklist c) {
        return conId(new ItemChecklist(c, "Limpiar plancha", true), 30L);
    }

    @Test
    void consultar_conTurnoYChecklist_buscaLosItemsActivosDeEseChecklist() {
        Turno t = turno();
        Checklist c = checklist(t);
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));
        when(checklists.findByTurno_IdAndActivoTrue(7L)).thenReturn(Optional.of(c));
        when(items.findByChecklist_IdAndActivoTrue(20L)).thenReturn(List.of(item(c)));

        ChecklistRespuesta respuesta = servicio.consultar(7L);

        assertNotNull(respuesta);
        verify(items).findByChecklist_IdAndActivoTrue(20L);
    }

    @Test
    void consultar_conTurnoInexistente_lanzaNoEncontradoSinBuscarChecklist() {
        when(turnos.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultar(99L));

        verifyNoInteractions(checklists, items);
    }

    @Test
    void consultar_conTurnoSinChecklist_lanzaNoEncontrado() {
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(turno()));
        when(checklists.findByTurno_IdAndActivoTrue(7L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.consultar(7L));

        verifyNoInteractions(items);
    }

    @Test
    void agregarItem_conDescripcionConEspacios_guardaElItemSinEspaciosYSinCompletar() {
        Turno t = turno();
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));
        when(checklists.findByTurno_IdAndActivoTrue(7L)).thenReturn(Optional.of(checklist(t)));
        when(items.save(any(ItemChecklist.class))).thenAnswer(inv -> {
            ItemChecklist guardado = inv.getArgument(0);
            ReflectionTestUtils.setField(guardado, "id", 30L);
            return guardado;
        });

        ItemChecklistRespuesta respuesta = servicio.agregarItem(7L, new CrearItemRequest("  Apagar la plancha  ", true));

        ArgumentCaptor<ItemChecklist> captor = ArgumentCaptor.forClass(ItemChecklist.class);
        verify(items).save(captor.capture());
        assertEquals("Apagar la plancha", captor.getValue().getDescripcion());
        assertTrue(captor.getValue().isCritico());
        assertFalse(captor.getValue().isCompletado());
        assertNotNull(respuesta);
    }

    @Test
    void agregarItem_conTurnoInexistente_lanzaNoEncontradoYNoGuarda() {
        when(turnos.findByIdAndActivoTrue(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> servicio.agregarItem(99L, new CrearItemRequest("Cualquier tarea", false)));

        verify(items, never()).save(any());
    }

    @Test
    void completarItem_conItemDelChecklist_loMarcaComoCompletado() {
        Turno t = turno();
        Checklist c = checklist(t);
        ItemChecklist pendiente = item(c);
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));
        when(checklists.findByTurno_IdAndActivoTrue(7L)).thenReturn(Optional.of(c));
        when(items.findByIdAndChecklist_IdAndActivoTrue(30L, 20L)).thenReturn(Optional.of(pendiente));

        servicio.completarItem(7L, 30L);

        assertTrue(pendiente.isCompletado());
    }

    @Test
    void completarItem_conItemInexistente_lanzaNoEncontrado() {
        Turno t = turno();
        when(turnos.findByIdAndActivoTrue(7L)).thenReturn(Optional.of(t));
        when(checklists.findByTurno_IdAndActivoTrue(7L)).thenReturn(Optional.of(checklist(t)));
        when(items.findByIdAndChecklist_IdAndActivoTrue(99L, 20L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.completarItem(7L, 99L));
    }
}
