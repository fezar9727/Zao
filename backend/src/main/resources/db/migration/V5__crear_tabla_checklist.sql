CREATE TABLE checklist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    turno_id BIGINT NOT NULL UNIQUE,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_checklist_turno FOREIGN KEY (turno_id) REFERENCES turno(id)
) ENGINE=InnoDB;

CREATE TABLE item_checklist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    checklist_id BIGINT NOT NULL,
    descripcion VARCHAR(200) NOT NULL,
    critico BOOLEAN NOT NULL DEFAULT FALSE,
    completado BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_item_checklist FOREIGN KEY (checklist_id) REFERENCES checklist(id)
) ENGINE=InnoDB;

CREATE INDEX idx_item_checklist_checklist ON item_checklist(checklist_id);
