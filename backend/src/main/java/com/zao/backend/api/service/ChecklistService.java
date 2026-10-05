package com.zao.backend.api.service;

import com.zao.backend.api.dto.ChecklistRespuesta;
import com.zao.backend.api.dto.CrearItemRequest;
import com.zao.backend.api.dto.ItemChecklistRespuesta;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.ChecklistRepository;
import com.zao.backend.api.repository.ItemChecklistRepository;
import com.zao.backend.api.repository.TurnoRepository;
import com.zao.backend.tareas.Checklist;
import com.zao.backend.tareas.ItemChecklist;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio del checklist de un turno: consultarlo, agregarle items y
 * marcar items como completados. Un item completado no se puede desmarcar.
 */
@Service
@Profile("servicios")
public class ChecklistService {

    private final TurnoRepository turnos;
    private final ChecklistRepository checklists;
    private final ItemChecklistRepository items;

    public ChecklistService(TurnoRepository turnos, ChecklistRepository checklists,
                            ItemChecklistRepository items) {
        this.turnos = turnos;
        this.checklists = checklists;
        this.items = items;
    }

    /** Consulta el checklist de un turno con sus items activos. */
    @Transactional(readOnly = true)
    public ChecklistRespuesta consultar(Long turnoId) {
        Checklist checklist = buscarChecklist(turnoId);
        return ChecklistRespuesta.desde(checklist, items.findByChecklist_IdAndActivoTrue(checklist.getId()));
    }

    /** Agrega un item al checklist de un turno. */
    @Transactional
    public ItemChecklistRespuesta agregarItem(Long turnoId, CrearItemRequest solicitud) {
        Checklist checklist = buscarChecklist(turnoId);
        ItemChecklist item = items.save(new ItemChecklist(checklist,
                solicitud.descripcion().trim(), solicitud.critico()));
        return ItemChecklistRespuesta.desde(item);
    }

    /** Marca un item del checklist como completado. */
    @Transactional
    public ItemChecklistRespuesta completarItem(Long turnoId, Long itemId) {
        Checklist checklist = buscarChecklist(turnoId);
        ItemChecklist item = items.findByIdAndChecklist_IdAndActivoTrue(itemId, checklist.getId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un item activo con id " + itemId + " en el checklist del turno " + turnoId + "."));
        item.marcarCompletado();
        return ItemChecklistRespuesta.desde(item);
    }

    private Checklist buscarChecklist(Long turnoId) {
        turnos.findByIdAndActivoTrue(turnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un turno activo con id " + turnoId + "."));
        return checklists.findByTurno_IdAndActivoTrue(turnoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El turno " + turnoId + " no tiene checklist."));
    }
}
