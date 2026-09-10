CREATE TABLE empleado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    nombre_completo VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    fecha_contratacion DATE,
    autorizacion_datos_aceptada BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_autorizacion_datos DATETIME NULL,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_empleado_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurante(id)
) ENGINE=InnoDB;

CREATE INDEX idx_empleado_restaurante ON empleado(restaurante_id);
