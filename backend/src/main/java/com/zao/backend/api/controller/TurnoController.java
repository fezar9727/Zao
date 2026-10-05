package com.zao.backend.api.controller;

import com.zao.backend.api.dto.ActualizarTurnoRequest;
import com.zao.backend.api.dto.CrearTurnoRequest;
import com.zao.backend.api.dto.TurnoRespuesta;
import com.zao.backend.api.service.TurnoService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Servicio web de turnos: listar, consultar, crear, actualizar y eliminar (logico). */
@RestController
@RequestMapping("/api/turnos")
@CrossOrigin(origins = "http://localhost:3000")
@Profile("servicios")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    /** GET /api/turnos: lista los turnos activos (opcional: ?restauranteId=1). */
    @GetMapping
    public List<TurnoRespuesta> listar(@RequestParam(required = false) Long restauranteId) {
        return turnoService.listar(restauranteId);
    }

    /** GET /api/turnos/{id}: consulta un turno (404 si no existe). */
    @GetMapping("/{id}")
    public TurnoRespuesta consultar(@PathVariable Long id) {
        return turnoService.consultar(id);
    }

    /** POST /api/turnos: crea un turno y su checklist vacio (201; 400, 404 o 409 si hay error). */
    @PostMapping
    public ResponseEntity<TurnoRespuesta> crear(@Valid @RequestBody CrearTurnoRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(turnoService.crear(solicitud));
    }

    /** PUT /api/turnos/{id}: actualiza estacion, fecha, horario y marcas. */
    @PutMapping("/{id}")
    public TurnoRespuesta actualizar(@PathVariable Long id,
                                     @Valid @RequestBody ActualizarTurnoRequest solicitud) {
        return turnoService.actualizar(id, solicitud);
    }

    /** DELETE /api/turnos/{id}: eliminacion logica, responde 204 (404 si no existe). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        turnoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
