package com.zao.backend.turnos.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Turno {
    private Long idTurno;
    private Long idEmpleado;
    private Long idEstacion;
    private LocalDate fechaTurno;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean checklistCompletado;

    public Turno() {
    }

    public Turno(Long idTurno, Long idEmpleado, Long idEstacion, LocalDate fechaTurno,
                 LocalTime horaInicio, LocalTime horaFin, boolean checklistCompletado) {
        this.idTurno = idTurno;
        this.idEmpleado = idEmpleado;
        this.idEstacion = idEstacion;
        this.fechaTurno = fechaTurno;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.checklistCompletado = checklistCompletado;
    }

    public Long getIdTurno() { return idTurno; }
    public void setIdTurno(Long idTurno) { this.idTurno = idTurno; }
    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }
    public Long getIdEstacion() { return idEstacion; }
    public void setIdEstacion(Long idEstacion) { this.idEstacion = idEstacion; }
    public LocalDate getFechaTurno() { return fechaTurno; }
    public void setFechaTurno(LocalDate fechaTurno) { this.fechaTurno = fechaTurno; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public boolean isChecklistCompletado() { return checklistCompletado; }
    public void setChecklistCompletado(boolean checklistCompletado) { this.checklistCompletado = checklistCompletado; }

    @Override
    public String toString() {
        return "Turno{id=" + idTurno + ", idEmpleado=" + idEmpleado + ", idEstacion=" + idEstacion
                + ", fecha=" + fechaTurno + ", inicio=" + horaInicio + ", fin=" + horaFin
                + ", checklistCompletado=" + checklistCompletado + "}";
    }
}