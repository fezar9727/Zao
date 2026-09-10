package com.zao.backend.tareas;

import com.zao.backend.common.EntidadBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Tarea individual dentro de un checklist de traspaso. El campo critico
 * determina si, al quedar sin completar, obliga a generar una
 * JustificacionPendiente (módulo de auditoría) para poder cerrar el turno.
 */
@Entity
@Table(name = "item_checklist")
public class ItemChecklist extends EntidadBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;

    @NotBlank
    @Size(max = 200)
    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @Column(name = "critico", nullable = false)
    private boolean critico;

    @Column(name = "completado", nullable = false)
    private boolean completado = false;

    protected ItemChecklist() {
        // Requerido por JPA
    }

    public ItemChecklist(Checklist checklist, String descripcion, boolean critico) {
        this.checklist = checklist;
        this.descripcion = descripcion;
        this.critico = critico;
    }

    public void marcarCompletado() {
        this.completado = true;
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCritico() {
        return critico;
    }

    public boolean isCompletado() {
        return completado;
    }
}
