package com.zao.backend.api.repository;

import com.zao.backend.restaurante.Restaurante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Acceso a datos de restaurantes (solo lectura en esta fase). */
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    /** Busca un restaurante que no haya sido eliminado logicamente. */
    Optional<Restaurante> findByIdAndActivoTrue(Long id);
}
