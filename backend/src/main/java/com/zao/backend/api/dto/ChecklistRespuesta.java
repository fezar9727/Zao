package com.zao.backend.api.dto;

import com.zao.backend.tareas.Checklist;
import com.zao.backend.tareas.ItemChecklist;

import java.util.List;

/** Checklist de un turno con sus items; completo = tiene items y todos estan completados. */
public record ChecklistRespuesta(
        Long id,
        Long turnoId,
        boolean completo,
        List<ItemChecklistRespuesta> items) {

    /** Arma la respuesta a partir del checklist y de sus items activos. */
    public static ChecklistRespuesta desde(Checklist checklist, List<ItemChecklist> items) {
        List<ItemChecklistRespuesta> lista = items.stream().map(ItemChecklistRespuesta::desde).toList();
        boolean completo = !items.isEmpty() && items.stream().allMatch(ItemChecklist::isCompletado);
        return new ChecklistRespuesta(checklist.getId(), checklist.getTurno().getId(), completo, lista);
    }
}
