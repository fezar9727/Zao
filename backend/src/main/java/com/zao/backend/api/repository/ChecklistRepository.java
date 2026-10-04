package com.zao.backend.api.repository;

import com.zao.backend.tareas.Checklist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Acceso a datos de checklists: cada turno tiene uno. */
public interface ChecklistRepository extends JpaRepository<Checklist, Long> {

    /** Busca el checklist activo de un turno. */
    Optional<Checklist> findByTurno_IdAndActivoTrue(Long turnoId);
}
