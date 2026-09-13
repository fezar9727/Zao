package com.zao.backend.turnos.api;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 * Capa de servicio del modulo de estaciones de trabajo: concentra la
 * logica de negocio y las validaciones antes de delegar en el repositorio.
 */
@Service
public class EstacionTrabajoService {

    private final EstacionTrabajoRepository repositorio;

    public EstacionTrabajoService(EstacionTrabajoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<EstacionTrabajo> listarTodas() {
        return repositorio.findAll();
    }

    public Optional<EstacionTrabajo> buscarPorId(Long id) {
        return repositorio.findById(id);
    }

    public EstacionTrabajo crear(EstacionTrabajo nueva) {
        if (nueva.getNombre() == null || nueva.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la estacion es obligatorio");
        }
        return repositorio.save(nueva);
    }

    public Optional<EstacionTrabajo> actualizar(Long id, EstacionTrabajo datos) {
        return repositorio.findById(id).map(estacionExistente -> {
            estacionExistente.setNombre(datos.getNombre());
            estacionExistente.setDescripcion(datos.getDescripcion());
            return repositorio.save(estacionExistente);
        });
    }

    public boolean eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            return false;
        }
        repositorio.deleteById(id);
        return true;
    }
}
