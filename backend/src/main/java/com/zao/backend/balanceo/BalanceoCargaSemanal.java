package com.zao.backend.balanceo;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.identidad.Empleado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Resumen semanal de carga de trabajo pesado por empleado. Se recalcula al
 * cerrar cada semana a partir del historial real de turnos y tareas
 * asignadas (el algoritmo de cálculo se implementa en Fase 2).
 *
 * Solo visible para administración/gerencia del restaurante — un empleado
 * nunca puede consultar el registro de otro empleado, según la decisión de
 * producto confirmada sobre privacidad de datos de desempeño.
 */
@Entity
@Table(name = "balanceo_carga_semanal")
public class BalanceoCargaSemanal extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @NotNull
    @Column(name = "semana_inicio", nullable = false)
    private LocalDate semanaInicio;

    @Min(0)
    @Column(name = "cantidad_tareas_pesadas", nullable = false)
    private int cantidadTareasPesadas;

    @Min(0)
    @Column(name = "horas_totales_turno", nullable = false)
    private int horasTotalesTurno;

    protected BalanceoCargaSemanal() {
        // Requerido por JPA
    }

    public BalanceoCargaSemanal(Empleado empleado, LocalDate semanaInicio,
                                 int cantidadTareasPesadas, int horasTotalesTurno) {
        this.empleado = empleado;
        this.semanaInicio = semanaInicio;
        this.cantidadTareasPesadas = cantidadTareasPesadas;
        this.horasTotalesTurno = horasTotalesTurno;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public LocalDate getSemanaInicio() {
        return semanaInicio;
    }

    public int getCantidadTareasPesadas() {
        return cantidadTareasPesadas;
    }

    public int getHorasTotalesTurno() {
        return horasTotalesTurno;
    }
}
