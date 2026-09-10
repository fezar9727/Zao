package com.zao.backend.turnos;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.restaurante.Restaurante;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Estación física de trabajo dentro de un restaurante (parrilla, salteados,
 * barra, caja, salón). Cada restaurante define sus propias estaciones porque
 * no todos los restaurantes tienen la misma distribución de cocina.
 */
@Entity
@Table(name = "estacion")
public class Estacion extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    @NotBlank
    @Size(max = 80)
    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    protected Estacion() {
        // Requerido por JPA
    }

    public Estacion(Restaurante restaurante, String nombre) {
        this.restaurante = restaurante;
        this.nombre = nombre;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
