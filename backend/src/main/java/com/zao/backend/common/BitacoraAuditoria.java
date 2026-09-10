package com.zao.backend.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Registro inmutable de cada creación, modificación o eliminación lógica
 * que ocurre en cualquier módulo del sistema.
 *
 * Deliberadamente NO extiende EntidadBase: esta tabla no tiene fecha de
 * actualización ni bandera "activo", porque una fila de auditoría nunca se
 * modifica ni se elimina, ni siquiera lógicamente. Es de solo-inserción
 * (append-only) a nivel de aplicación; en Fase 2 esto se refuerza además a
 * nivel de base de datos revocando permisos UPDATE/DELETE al usuario de la
 * aplicación sobre esta tabla específica.
 *
 * Por qué importa jurídicamente: la Ley 527 de 1999 (artículo 9) le da valor
 * probatorio pleno a un mensaje de datos siempre que se demuestre que
 * permaneció íntegro e inalterado. Un registro que nadie puede editar
 * después de creado es la forma más simple de sostener esa integridad ante
 * una eventual disputa laboral o legal.
 */
@Entity
@Table(name = "bitacora_auditoria")
public class BitacoraAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "restaurante_id", nullable = false)
    private Long restauranteId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "nombre_entidad", nullable = false, length = 100)
    private String nombreEntidad;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_accion", nullable = false, length = 30)
    private TipoAccionAuditoria tipoAccion;

    @Column(name = "valor_anterior", columnDefinition = "LONGTEXT")
    private String valorAnterior;

    @Column(name = "valor_nuevo", columnDefinition = "LONGTEXT")
    private String valorNuevo;

    @Column(name = "fecha_evento", nullable = false, updatable = false)
    private LocalDateTime fechaEvento;

    protected BitacoraAuditoria() {
        // Requerido por JPA
    }

    public BitacoraAuditoria(Long restauranteId, Long usuarioId, String nombreEntidad,
                              Long entidadId, TipoAccionAuditoria tipoAccion,
                              String valorAnterior, String valorNuevo) {
        this.restauranteId = restauranteId;
        this.usuarioId = usuarioId;
        this.nombreEntidad = nombreEntidad;
        this.entidadId = entidadId;
        this.tipoAccion = tipoAccion;
        this.valorAnterior = valorAnterior;
        this.valorNuevo = valorNuevo;
        this.fechaEvento = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getRestauranteId() {
        return restauranteId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombreEntidad() {
        return nombreEntidad;
    }

    public Long getEntidadId() {
        return entidadId;
    }

    public TipoAccionAuditoria getTipoAccion() {
        return tipoAccion;
    }

    public String getValorAnterior() {
        return valorAnterior;
    }

    public String getValorNuevo() {
        return valorNuevo;
    }

    public LocalDateTime getFechaEvento() {
        return fechaEvento;
    }
}