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
 * Mapea la tabla real "estacion_trabajo" de la base de datos academica
 * "zao", creada en AA2-EV01: id_estacion (llave primaria autoincremental),
 * nombre y descripcion (confirmado con DESCRIBE contra la base real).
 * A diferencia del modulo de AA2-EV01 (que manipula esta misma tabla con
 * JDBC puro por medio de la clase
 * com.zao.backend.turnos.modelo.EstacionTrabajo), esta clase usa
 * anotaciones de Jakarta Persistence para que Hibernate, a traves de
 * Spring Data JPA, se encargue automaticamente del mapeo objeto-relacional
 * y de las operaciones CRUD.
 */
@Entity
@Table(name = "estacion_trabajo")
public class EstacionTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estacion")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    public EstacionTrabajo() {
    }

    public EstacionTrabajo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
