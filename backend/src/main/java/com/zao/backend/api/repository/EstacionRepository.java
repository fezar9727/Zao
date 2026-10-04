package com.zao.backend.api.repository;

import com.zao.backend.turnos.Estacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Acceso a datos de estaciones de trabajo (tabla estacion). */
public interface EstacionRepository extends JpaRepository<Estacion, Long> {

    /** Busca una estacion activa por su identificador. */
    Optional<Estacion> findByIdAndActivoTrue(Long id);
}
