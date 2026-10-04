package com.zao.backend.api.dto;

import com.zao.backend.turnos.EstadoTurno;
import com.zao.backend.turnos.Turno;

import java.time.LocalDate;
import java.time.LocalTime;

/** Datos de un turno que la API devuelve al cliente. */
public record TurnoRespuesta(
        Long id,
        Long restauranteId,
        Long empleadoId,
        Long estacionId,
        LocalDate fechaTurno,
        LocalTime horaInicio,
        LocalTime horaFin,
        boolean esNocturno,
        boolean esFestivo,
        EstadoTurno estado) {

    /** Convierte la entidad en la respuesta que se envia al cliente. */
    public static TurnoRespuesta desde(Turno turno) {
        return new TurnoRespuesta(
                turno.getId(),
                turno.getRestaurante().getId(),
                turno.getEmpleado().getId(),
                turno.getEstacion().getId(),
                turno.getFechaTurno(),
                turno.getHoraInicio(),
                turno.getHoraFin(),
                turno.isEsNocturno(),
                turno.isEsFestivo(),
                turno.getEstado());
    }
}
