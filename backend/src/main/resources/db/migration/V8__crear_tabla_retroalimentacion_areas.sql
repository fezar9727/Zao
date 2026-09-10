CREATE TABLE retroalimentacion_areas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    turno_id BIGINT NOT NULL,
    area_origen VARCHAR(60) NOT NULL,
    area_destino VARCHAR(60) NOT NULL,
    calificacion INT NOT NULL,
    comentario VARCHAR(300),
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_retro_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id),
    CONSTRAINT fk_retro_turno FOREIGN KEY (turno_id) REFERENCES turno(id),
    CONSTRAINT chk_calificacion_rango CHECK (calificacion BETWEEN 1 AND 5)
) ENGINE=InnoDB;
