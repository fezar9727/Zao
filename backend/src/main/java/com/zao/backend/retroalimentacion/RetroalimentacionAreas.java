package com.zao.backend.retroalimentacion;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.restaurante.Restaurante;
import com.zao.backend.turnos.Turno;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Calificación rápida de coordinación entre áreas (ej. cocina y salón) al
 * cierre de un turno. Alimenta la métrica de fricción operativa a lo largo
 * del tiempo (el cálculo agregado se hace en Fase 2).
 */
@Entity
@Table(name = "retroalimentacion_areas")
public class RetroalimentacionAreas extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;

    @NotBlank
    @Size(max = 60)
    @Column(name = "area_origen", nullable = false, length = 60)
    private String areaOrigen;

    @NotBlank
    @Size(max = 60)
    @Column(name = "area_destino", nullable = false, length = 60)
    private String areaDestino;

    @Min(1)
    @Max(5)
    @Column(name = "calificacion", nullable = false)
    private int calificacion;

    @Size(max = 300)
    @Column(name = "comentario", length = 300)
    private String comentario;

    protected RetroalimentacionAreas() {
        // Requerido por JPA
    }

    public RetroalimentacionAreas(Restaurante restaurante, Turno turno, String areaOrigen,
                                   String areaDestino, int calificacion, String comentario) {
        this.restaurante = restaurante;
        this.turno = turno;
        this.areaOrigen = areaOrigen;
        this.areaDestino = areaDestino;
        this.calificacion = calificacion;
        this.comentario = comentario;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public Turno getTurno() {
        return turno;
    }

    public String getAreaOrigen() {
        return areaOrigen;
    }

    public String getAreaDestino() {
        return areaDestino;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public String getComentario() {
        return comentario;
    }
}
