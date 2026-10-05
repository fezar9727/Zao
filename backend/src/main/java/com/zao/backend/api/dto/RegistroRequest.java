package com.zao.backend.api.dto;

import com.zao.backend.identidad.RolEmpleado;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/** Cuerpo de la peticion de registro de un empleado. */
public record RegistroRequest(
        @NotBlank @Size(max = 120) String nombreCompleto,
        @NotBlank @Email @Size(max = 150) String correo,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotNull RolEmpleado rol,
        @NotNull @Positive Long restauranteId,
        LocalDate fechaContratacion,
        @AssertTrue(message = "Debe aceptar el tratamiento de datos personales.") boolean aceptaTratamientoDatos) {
}
