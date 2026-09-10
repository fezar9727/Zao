CREATE TABLE turno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    empleado_id BIGINT NOT NULL,
    estacion_id BIGINT NOT NULL,
    fecha_turno DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    es_nocturno BOOLEAN NOT NULL DEFAULT FALSE,
    es_festivo BOOLEAN NOT NULL DEFAULT FALSE,
    estado VARCHAR(20) NOT NULL DEFAULT 'PROGRAMADO',
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_turno_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id),
    CONSTRAINT fk_turno_empleado FOREIGN KEY (empleado_id) REFERENCES empleado(id),
    CONSTRAINT fk_turno_estacion FOREIGN KEY (estacion_id) REFERENCES estacion(id)
) ENGINE=InnoDB;

CREATE INDEX idx_turno_restaurante ON turno(restaurante_id);
CREATE INDEX idx_turno_empleado ON turno(empleado_id);
CREATE INDEX idx_turno_fecha ON turno(fecha_turno);
