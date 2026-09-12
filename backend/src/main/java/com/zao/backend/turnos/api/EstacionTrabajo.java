package com.zao.backend.turnos.api;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una estacion de trabajo dentro de la cocina
 * (por ejemplo: Parrilla, Fritura, Pasteleria, Ensaladas, etc).
 *
 * A diferencia del modulo de AA2-EV01 (que manipula esta misma tabla con
 * JDBC puro por medio de la clase com.zao.backend.turnos.modelo.EstacionTrabajo),
 * esta clase usa anotaciones de Jakarta Persistence para que Hibernate,
 * a traves de Spring Data JPA, se encargue automaticamente del mapeo
 * objeto-relacional y de las operaciones CRUD sobre la tabla estacion_trabajo.
 */
@Entity
@Table(name = "estacion_trabajo")
public class EstacionTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @Column(name = "activa", nullable = false)
    private boolean activa;

    public EstacionTrabajo() {
    }

    public EstacionTrabajo(String nombre, boolean activa) {
        this.nombre = nombre;
        this.activa = activa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
