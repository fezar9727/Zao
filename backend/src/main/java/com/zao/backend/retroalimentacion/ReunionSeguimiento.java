package com.zao.backend.retroalimentacion;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.restaurante.Restaurante;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Reunión de seguimiento organizada por un administrador.
 *
 * Si tipo = INDIVIDUAL: debe tener exactamente un empleado en "asistentes",
 * y notasPrivadas solo puede ser leído por ese empleado y por el admin que
 * la creó (regla que se aplica en el service de Fase 2, no acá).
 *
 * Si tipo = GRUPAL: puede tener varios asistentes y no debería contener
 * comentarios sobre el desempeño individual de nadie — es para briefings,
 * capacitaciones o coordinación operativa de equipo, no para evaluación.
 */
@Entity
@Table(name = "reunion_seguimiento")
public class ReunionSeguimiento extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private Empleado admin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoReunion tipo;

    @NotNull
    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "notas_privadas", columnDefinition = "TEXT")
    private String notasPrivadas;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
        name = "reunion_asistente",
        joinColumns = @JoinColumn(name = "reunion_id"),
        inverseJoinColumns = @JoinColumn(name = "empleado_id")
    )
    private Set<Empleado> asistentes = new HashSet<>();

    protected ReunionSeguimiento() {
        // Requerido por JPA
    }

    public ReunionSeguimiento(Restaurante restaurante, Empleado admin, TipoReunion tipo,
                               LocalDateTime fechaHora, String notasPrivadas) {
        this.restaurante = restaurante;
        this.admin = admin;
        this.tipo = tipo;
        this.fechaHora = fechaHora;
        this.notasPrivadas = notasPrivadas;
    }

    public void agregarAsistente(Empleado empleado) {
        this.asistentes.add(empleado);
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Empleado getAdmin() {
        return admin;
    }

    public TipoReunion getTipo() {
        return tipo;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getNotasPrivadas() {
        return notasPrivadas;
    }

    public Set<Empleado> getAsistentes() {
        return asistentes;
    }
}