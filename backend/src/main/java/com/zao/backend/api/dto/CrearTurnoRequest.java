package com.zao.backend.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

/** Cuerpo de la peticion para crear un turno. El restaurante se toma del empleado. */
public record CrearTurnoRequest(
        @NotNull @Positive Long empleadoId,
        @NotNull @Positive Long estacionId,
        @NotNull LocalDate fechaTurno,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        boolean esNocturno,
        boolean esFestivo) {
}
