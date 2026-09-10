package com.zao.backend.auditoria;

import com.zao.backend.identidad.Empleado;
import com.zao.backend.tareas.ItemChecklist;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * Justificación que un empleado debe escribir cuando cierra su turno con un
 * ItemChecklist crítico sin completar. Es el módulo diferencial de todo el
 * proyecto: no existe para castigar, existe para que haya trazabilidad real
 * de qué pasó y por qué.
 *
 * Al igual que BitacoraAuditoria, esta entidad es deliberadamente inmutable:
 * no extiende EntidadBase, no tiene setters, y no existe ningún método para
 * modificar el texto una vez creada. Si la gerencia necesita agregar un
 * comentario de seguimiento sobre una justificación ya escrita, eso se
 * modela como un registro nuevo (a definir en Fase 2), nunca como una
 * edición del original — el valor de esta tabla como evidencia depende
 * exactamente de que nadie pueda alterarla después del hecho.
 */
@Entity
@Table(name = "justificacion_pendiente")
public class JustificacionPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_checklist_id", nullable = false)
    private ItemChecklist itemChecklist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @NotBlank
    @Column(name = "motivo", nullable = false, columnDefinition = "TEXT")
    private String motivo;

    @Column(name = "revisada_por_gerencia", nullable = false)
    private boolean revisadaPorGerencia = false;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    protected JustificacionPendiente() {
        // Requerido por JPA
    }

    public JustificacionPendiente(ItemChecklist itemChecklist, Empleado empleado, String motivo) {
        this.itemChecklist = itemChecklist;
        this.empleado = empleado;
        this.motivo = motivo;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * Único cambio de estado permitido sobre esta entidad: gerencia puede
     * marcarla como revisada. Esto no altera el motivo original ni quién lo
     * escribió, solo agrega el hecho de que alguien la leyó.
     */
    public void marcarRevisada() {
        this.revisadaPorGerencia = true;
    }

    public Long getId() {
        return id;
    }

    public ItemChecklist getItemChecklist() {
        return itemChecklist;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String getMotivo() {
        return motivo;
    }

    public boolean isRevisadaPorGerencia() {
        return revisadaPorGerencia;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}