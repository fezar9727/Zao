CREATE TABLE estacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_estacion_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
) ENGINE=InnoDB;

CREATE INDEX idx_estacion_restaurante ON estacion(restaurante_id);
