package com.zao.backend.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

/** Cuerpo de la peticion para actualizar un turno (el empleado no se puede cambiar). */
public record ActualizarTurnoRequest(
        @NotNull @Positive Long estacionId,
        @NotNull LocalDate fechaTurno,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        boolean esNocturno,
        boolean esFestivo) {
}
