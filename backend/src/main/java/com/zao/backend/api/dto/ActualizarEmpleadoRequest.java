package com.zao.backend.api.dto;

import com.zao.backend.identidad.RolEmpleado;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/** Cuerpo de la peticion para actualizar un empleado. */
public record ActualizarEmpleadoRequest(
        @NotBlank @Size(max = 120) String nombreCompleto,
        @NotNull RolEmpleado rol) {
}
