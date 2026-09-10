package com.zao.backend.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Superclase de la que heredan TODAS las entidades del sistema.
 *
 * Por qué existe: el proyecto exige que ningún registro se elimine físicamente
 * de la base de datos (borrado lógico) y que exista trazabilidad de creación y
 * modificación en cada tabla, sin excepción. Centralizar esto acá evita
 * duplicar estos mismos cinco campos en cada una de las entidades de negocio,
 * y garantiza que ningún desarrollador futuro "se olvide" de implementarlo
 * en una tabla nueva.
 *
 * El borrado real de un registro NUNCA se hace con DELETE. Se hace marcando
 * activo = false y completando eliminadoEn / eliminadoPor. El registro sigue
 * existiendo indefinidamente en la base de datos como respaldo legal
 * (Decreto 1377 de 2013, artículo 9: la supresión de un dato no procede
 * cuando existe un deber legal o contractual de conservarlo).
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntidadBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "eliminado_en")
    private LocalDateTime eliminadoEn;

    @Column(name = "eliminado_por")
    private Long eliminadoPor;

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public LocalDateTime getEliminadoEn() {
        return eliminadoEn;
    }

    public Long getEliminadoPor() {
        return eliminadoPor;
    }

    /**
     * Marca la entidad como eliminada lógicamente. No borra la fila.
     * Debe ser el único punto de entrada para "eliminar" cualquier entidad
     * del sistema; nunca se debe invocar un repository.delete(...) real
     * sobre una entidad de negocio.
     */
    public void marcarComoEliminado(Long idUsuarioQueElimina) {
        this.activo = false;
        this.eliminadoEn = LocalDateTime.now();
        this.eliminadoPor = idUsuarioQueElimina;
    }
}
