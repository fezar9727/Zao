package com.zao.backend.turnos.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio Spring Data JPA para la entidad EstacionTrabajo.
 *
 * Al extender JpaRepository, Spring genera automaticamente en tiempo de
 * ejecucion la implementacion de las operaciones CRUD basicas, sin
 * necesidad de escribir sentencias SQL ni codigo JDBC manual, a diferencia
 * del TurnoRepository de AA2-EV01 que si las escribe explicitamente.
 */
@Repository
public interface EstacionTrabajoRepository extends JpaRepository<EstacionTrabajo, Long> {
}
