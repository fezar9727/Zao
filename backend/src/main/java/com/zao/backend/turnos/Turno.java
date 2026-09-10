package com.zao.backend.turnos;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.restaurante.Restaurante;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Turno de trabajo de un empleado en una estación específica.
 *
 * Los campos esNocturno / esFestivo no calculan nómina (Zào no es un
 * sistema de nómina), pero se guardan porque desde julio de 2026 la jornada
 * laboral en Colombia bajó a 42 horas semanales (Ley 2101 de 2021) y el
 * recargo nocturno aplica de 7:00 p.m. a 6:00 a.m. — dejar esta marca desde
 * el modelo de datos evita tener que rediseñar la tabla el día que se
 * construya un módulo de reportes o una integración con nómina externa.
 */
@Entity
@Table(name = "turno")
public class Turno extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estacion_id", nullable = false)
    private Estacion estacion;

    @NotNull
    @Column(name = "fecha_turno", nullable = false)
    private LocalDate fechaTurno;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    @Column(name = "es_nocturno", nullable = false)
    private boolean esNocturno;

    @Column(name = "es_festivo", nullable = false)
    private boolean esFestivo;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoTurno estado = EstadoTurno.PROGRAMADO;

    protected Turno() {
        // Requerido por JPA
    }

    public Turno(Restaurante restaurante, Empleado empleado, Estacion estacion,
                 LocalDate fechaTurno, LocalTime horaInicio, LocalTime horaFin,
                 boolean esNocturno, boolean esFestivo) {
        this.restaurante = restaurante;
        this.empleado = empleado;
        this.estacion = estacion;
        this.fechaTurno = fechaTurno;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.esNocturno = esNocturno;
        this.esFestivo = esFestivo;
    }

    public void iniciar() {
        this.estado = EstadoTurno.EN_CURSO;
    }

    public void cerrar() {
        this.estado = EstadoTurno.CERRADO;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Estacion getEstacion() {
        return estacion;
    }

    public LocalDate getFechaTurno() {
        return fechaTurno;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public boolean isEsNocturno() {
        return esNocturno;
    }

    public boolean isEsFestivo() {
        return esFestivo;
    }

    public EstadoTurno getEstado() {
        return estado;
    }
}
