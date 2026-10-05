package com.zao.backend.api.repository;

import com.zao.backend.identidad.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/** Acceso a datos de empleados mediante Spring Data JPA (consultas parametrizadas). */
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    /** Indica si el correo ya esta en uso (incluye empleados eliminados logicamente). */
    boolean existsByCorreo(String correo);

    /** Busca un empleado activo por correo, para el inicio de sesion. */
    Optional<Empleado> findByCorreoAndActivoTrue(String correo);

    /** Busca un empleado activo por su identificador. */
    Optional<Empleado> findByIdAndActivoTrue(Long id);

    /** Lista todos los empleados activos. */
    List<Empleado> findByActivoTrue();

    /** Lista los empleados activos de un restaurante. */
    List<Empleado> findByRestaurante_IdAndActivoTrue(Long restauranteId);
}
