package com.zao.backend.api.controller;

import com.zao.backend.api.dto.ChecklistRespuesta;
import com.zao.backend.api.dto.CrearItemRequest;
import com.zao.backend.api.dto.ItemChecklistRespuesta;
import com.zao.backend.api.service.ChecklistService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Servicio web del checklist de un turno: consultar, agregar items y completarlos. */
@RestController
@RequestMapping("/api/turnos/{turnoId}/checklist")
@CrossOrigin(origins = "http://localhost:3000")
@Profile("servicios")
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    /** GET /api/turnos/{turnoId}/checklist: consulta el checklist con sus items (404 si no existe). */
    @GetMapping
    public ChecklistRespuesta consultar(@PathVariable Long turnoId) {
        return checklistService.consultar(turnoId);
    }

    /** POST /api/turnos/{turnoId}/checklist/items: agrega un item (201; 400 o 404 si hay error). */
    @PostMapping("/items")
    public ResponseEntity<ItemChecklistRespuesta> agregarItem(@PathVariable Long turnoId,
                                                              @Valid @RequestBody CrearItemRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(checklistService.agregarItem(turnoId, solicitud));
    }

    /** PUT /api/turnos/{turnoId}/checklist/items/{itemId}/completar: marca el item como completado. */
    @PutMapping("/items/{itemId}/completar")
    public ItemChecklistRespuesta completarItem(@PathVariable Long turnoId, @PathVariable Long itemId) {
        return checklistService.completarItem(turnoId, itemId);
    }
}
