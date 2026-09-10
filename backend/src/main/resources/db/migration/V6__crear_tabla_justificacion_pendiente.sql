-- Tabla deliberadamente sin columnas activo/eliminado_en/eliminado_por:
-- una justificación de pendiente nunca se elimina, ni siquiera lógicamente.
CREATE TABLE justificacion_pendiente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_checklist_id BIGINT NOT NULL,
    empleado_id BIGINT NOT NULL,
    motivo TEXT NOT NULL,
    revisada_por_gerencia BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion DATETIME NOT NULL,
    CONSTRAINT fk_justificacion_item FOREIGN KEY (item_checklist_id) REFERENCES item_checklist(id),
    CONSTRAINT fk_justificacion_empleado FOREIGN KEY (empleado_id) REFERENCES empleado(id)
) ENGINE=InnoDB;

CREATE INDEX idx_justificacion_empleado ON justificacion_pendiente(empleado_id);
