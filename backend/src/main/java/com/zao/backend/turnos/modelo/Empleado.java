package com.zao.backend.turnos.modelo;

public class Empleado {
    private Long idEmpleado;
    private String nombre;
    private String correo;
    private Rol rol;

    public Empleado() {
    }

    public Empleado(Long idEmpleado, String nombre, String correo, Rol rol) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }

    public Long getIdEmpleado() { return idEmpleado; }
    public void setIdEmpleado(Long idEmpleado) { this.idEmpleado = idEmpleado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    @Override
    public String toString() {
        return "Empleado{id=" + idEmpleado + ", nombre='" + nombre + "', correo='" + correo + "', rol=" + rol + "}";
    }
}