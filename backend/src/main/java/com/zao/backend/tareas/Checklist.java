package com.zao.backend.tareas;

import com.zao.backend.common.EntidadBase;
import com.zao.backend.turnos.Turno;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Checklist de traspaso asociado a un turno específico. Un turno no puede
 * pasar a estado CERRADO (ver Turno.cerrar()) sin que este checklist esté
 * completo o tenga sus pendientes justificados a través del módulo de
 * auditoría — esa regla de negocio se implementa en el service de Fase 2,
 * acá solo se modela la estructura de datos.
 */
@Entity
@Table(name = "checklist")
public class Checklist extends EntidadBase {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false, unique = true)
    private Turno turno;

    @OneToMany(mappedBy = "checklist", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<ItemChecklist> items = new ArrayList<>();

    protected Checklist() {
        // Requerido por JPA
    }

    public Checklist(Turno turno) {
        this.turno = turno;
    }

    public Turno getTurno() {
        return turno;
    }

    public List<ItemChecklist> getItems() {
        return items;
    }

    public boolean estaCompleto() {
        return items.stream().allMatch(ItemChecklist::isCompletado);
    }
}
