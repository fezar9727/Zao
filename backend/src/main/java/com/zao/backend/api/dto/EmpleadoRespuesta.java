package com.zao.backend.api.dto;

import com.zao.backend.identidad.Empleado;
import com.zao.backend.identidad.RolEmpleado;

import java.time.LocalDate;

/**
 * Datos de un empleado que la API devuelve al cliente.
 * No incluye la contrasena ni su hash: nunca salen por la API.
 */
public record EmpleadoRespuesta(
        Long id,
        Long restauranteId,
        String nombreCompleto,
        String correo,
        RolEmpleado rol,
        LocalDate fechaContratacion,
        boolean autorizacionDatosAceptada) {

    /** Convierte la entidad en la respuesta que se envia al cliente. */
    public static EmpleadoRespuesta desde(Empleado empleado) {
        return new EmpleadoRespuesta(
                empleado.getId(),
                empleado.getRestaurante().getId(),
                empleado.getNombreCompleto(),
                empleado.getCorreo(),
                empleado.getRol(),
                empleado.getFechaContratacion(),
                empleado.isAutorizacionDatosAceptada());
    }
}
