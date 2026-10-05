package com.zao.backend.api.dto;

/** Respuesta de un inicio de sesion correcto. */
public record RespuestaLogin(
        boolean exito,
        String mensaje,
        EmpleadoRespuesta empleado) {
}
