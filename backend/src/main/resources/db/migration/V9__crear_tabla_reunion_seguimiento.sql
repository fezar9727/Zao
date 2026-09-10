CREATE TABLE reunion_seguimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    admin_id BIGINT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    notas_privadas TEXT,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_reunion_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id),
    CONSTRAINT fk_reunion_admin FOREIGN KEY (admin_id) REFERENCES empleado(id)
) ENGINE=InnoDB;

-- Tabla intermedia de asistentes. Si tipo = INDIVIDUAL debe tener exactamente
-- un empleado; esa regla se valida en el service de Fase 2, no a nivel de BD.
CREATE TABLE reunion_asistente (
    reunion_id BIGINT NOT NULL,
    empleado_id BIGINT NOT NULL,
    PRIMARY KEY (reunion_id, empleado_id),
    CONSTRAINT fk_asistente_reunion FOREIGN KEY (reunion_id) REFERENCES reunion_seguimiento(id),
    CONSTRAINT fk_asistente_empleado FOREIGN KEY (empleado_id) REFERENCES empleado(id)
) ENGINE=InnoDB;
