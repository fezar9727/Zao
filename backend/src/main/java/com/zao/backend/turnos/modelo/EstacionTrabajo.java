package com.zao.backend.turnos.modelo;

public class EstacionTrabajo {
    private Long idEstacion;
    private String nombre;
    private String descripcion;

    public EstacionTrabajo() {
    }

    public EstacionTrabajo(Long idEstacion, String nombre, String descripcion) {
        this.idEstacion = idEstacion;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Long getIdEstacion() { return idEstacion; }
    public void setIdEstacion(Long idEstacion) { this.idEstacion = idEstacion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "EstacionTrabajo{id=" + idEstacion + ", nombre='" + nombre + "', descripcion='" + descripcion + "'}";
    }
}