package com.zao.backend.identidad;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.restaurante.Restaurante;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Empleado de un restaurante específico. La contraseña nunca se expone en
 * un getter que pueda serializarse hacia el cliente; su manejo (BCrypt,
 * JWT) se implementa en Fase 2.
 *
 * Los campos autorizacionDatosAceptada / fechaAutorizacionDatos existen
 * porque la Ley 1581 de 2012 (artículo 9) exige autorización previa e
 * informada del titular para tratar sus datos personales, y esa
 * autorización debe poder consultarse después — por eso se guarda con
 * fecha exacta y nunca se sobrescribe una vez aceptada.
 */
@Entity
@Table(name = "empleado")
public class Empleado extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @NotBlank
    @Size(max = 120)
    @Column(name = "nombre_completo", nullable = false, length = 120)
    private String nombreCompleto;

    @NotBlank
    @Email
    @Size(max = 150)
    @Column(name = "correo", nullable = false, unique = true, length = 150)
    private String correo;

    @NotBlank
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 30)
    private RolEmpleado rol;

    @Column(name = "fecha_contratacion")
    private LocalDate fechaContratacion;

    @Column(name = "autorizacion_datos_aceptada", nullable = false)
    private boolean autorizacionDatosAceptada = false;

    @Column(name = "fecha_autorizacion_datos")
    private LocalDateTime fechaAutorizacionDatos;

    protected Empleado() {
        // Requerido por JPA
    }

    public Empleado(Restaurante restaurante, String nombreCompleto, String correo,
                     String passwordHash, RolEmpleado rol, LocalDate fechaContratacion) {
        this.restaurante = restaurante;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.fechaContratacion = fechaContratacion;
    }

    /**
     * Registra la aceptación de la autorización de tratamiento de datos.
     * Es intencional que no exista un método para "desaceptar" o borrar
     * este registro: si un empleado revoca su autorización, eso se maneja
     * como un evento nuevo en la bitácora de auditoría, nunca sobrescribiendo
     * ni borrando la aceptación original.
     */
    public void aceptarAutorizacionDatos() {
        this.autorizacionDatosAceptada = true;
        this.fechaAutorizacionDatos = LocalDateTime.now();
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public RolEmpleado getRol() {
        return rol;
    }

    public void setRol(RolEmpleado rol) {
        this.rol = rol;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public boolean isAutorizacionDatosAceptada() {
        return autorizacionDatosAceptada;
    }

    public LocalDateTime getFechaAutorizacionDatos() {
        return fechaAutorizacionDatos;
    }
}
