package com.zao.backend.api.repository;

import com.zao.backend.turnos.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Acceso a datos de turnos mediante Spring Data JPA (consultas parametrizadas). */
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    /** Busca un turno activo por su identificador. */
    Optional<Turno> findByIdAndActivoTrue(Long id);

    /** Lista todos los turnos activos. */
    List<Turno> findByActivoTrue();

    /** Lista los turnos activos de un restaurante. */
    List<Turno> findByRestaurante_IdAndActivoTrue(Long restauranteId);
}
