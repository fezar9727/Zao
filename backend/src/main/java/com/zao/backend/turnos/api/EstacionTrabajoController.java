package com.zao.backend.turnos.api;

import java.util.List;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST del modulo de estaciones de trabajo, bajo /api/estaciones.
 * Este es el controlador que se probo con Postman en AA5 y que consume
 * el front-end en React de AA4.
 *
 * @CrossOrigin habilita las peticiones desde el frontend React academico
 * (localhost:3000). Se aplica UNICAMENTE en este controlador, que ya vive
 * aislado en el paquete academico com.zao.backend.turnos.api -- no afecta
 * ni modifica ningun otro controlador del proyecto real de Zao.
 */
@RestController
@RequestMapping("/api/estaciones")
@CrossOrigin(origins = "http://localhost:3000")
public class EstacionTrabajoController {

    private final EstacionTrabajoService servicio;

    public EstacionTrabajoController(EstacionTrabajoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<EstacionTrabajo> listar() {
        return servicio.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstacionTrabajo> buscarPorId(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EstacionTrabajo> crear(@RequestBody EstacionTrabajo nueva) {
        EstacionTrabajo creada = servicio.crear(nueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstacionTrabajo> actualizar(
            @PathVariable Long id, @RequestBody EstacionTrabajo datos) {
        return servicio.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminada = servicio.eliminar(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
