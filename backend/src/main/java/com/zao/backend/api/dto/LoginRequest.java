package com.zao.backend.api.dto;

import jakarta.validation.constraints.NotBlank;

/** Cuerpo de la peticion de inicio de sesion. */
public record LoginRequest(
        @NotBlank String correo,
        @NotBlank String password) {
}
