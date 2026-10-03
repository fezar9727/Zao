package com.zao.backend.api.controller;

import com.zao.backend.api.dto.ActualizarEmpleadoRequest;
import com.zao.backend.api.dto.EmpleadoRespuesta;
import com.zao.backend.api.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Servicio web de empleados: listar, consultar, actualizar y eliminar (logico). */
@RestController
@RequestMapping("/api/empleados")
@CrossOrigin(origins = "http://localhost:3000")
@Profile("servicios")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    /** GET /api/empleados: lista los empleados activos (opcional: ?restauranteId=1). */
    @GetMapping
    public List<EmpleadoRespuesta> listar(@RequestParam(required = false) Long restauranteId) {
        return empleadoService.listar(restauranteId);
    }

    /** GET /api/empleados/{id}: consulta un empleado (404 si no existe). */
    @GetMapping("/{id}")
    public EmpleadoRespuesta consultar(@PathVariable Long id) {
        return empleadoService.consultar(id);
    }

    /** PUT /api/empleados/{id}: actualiza nombre y rol (400 si hay datos invalidos, 404 si no existe). */
    @PutMapping("/{id}")
    public EmpleadoRespuesta actualizar(@PathVariable Long id,
                                        @Valid @RequestBody ActualizarEmpleadoRequest solicitud) {
        return empleadoService.actualizar(id, solicitud);
    }

    /** DELETE /api/empleados/{id}: eliminacion logica, responde 204 (404 si no existe). */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empleadoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
