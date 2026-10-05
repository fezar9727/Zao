package com.zao.backend.api.dto;

import com.zao.backend.tareas.ItemChecklist;

/** Datos de un item de checklist que la API devuelve al cliente. */
public record ItemChecklistRespuesta(
        Long id,
        String descripcion,
        boolean critico,
        boolean completado) {

    /** Convierte la entidad en la respuesta que se envia al cliente. */
    public static ItemChecklistRespuesta desde(ItemChecklist item) {
        return new ItemChecklistRespuesta(item.getId(), item.getDescripcion(),
                item.isCritico(), item.isCompletado());
    }
}
