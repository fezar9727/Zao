package com.zao.backend.restaurante;

import com.zao.backend.common.EntidadBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad raíz de todo el sistema multi-tenant: cada restaurante cliente es
 * un tenant aislado. Empleados, turnos, checklists, auditorías, balanceo y
 * retroalimentación siempre cuelgan de un restaurante_id, y ninguna consulta
 * de negocio debe poder cruzar datos entre dos restaurantes distintos.
 *
 * El NIT se guarda porque es el identificador legal real del negocio en
 * Colombia y sirve para no duplicar accidentalmente el mismo restaurante
 * dos veces en el sistema.
 */
@Entity
@Table(name = "restaurante")
public class Restaurante extends EntidadBase {

    @NotBlank
    @Size(max = 150)
    @Column(name = "nombre_comercial", nullable = false, length = 150)
    private String nombreComercial;

    @NotBlank
    @Size(max = 20)
    @Column(name = "nit", nullable = false, unique = true, length = 20)
    private String nit;

    @Size(max = 150)
    @Column(name = "direccion", length = 150)
    private String direccion;

    @Size(max = 80)
    @Column(name = "ciudad", length = 80)
    private String ciudad;

    protected Restaurante() {
        // Requerido por JPA
    }

    public Restaurante(String nombreComercial, String nit, String direccion, String ciudad) {
        this.nombreComercial = nombreComercial;
        this.nit = nit;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getNit() {
        return nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
