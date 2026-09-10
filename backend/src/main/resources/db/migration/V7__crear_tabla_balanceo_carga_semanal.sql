CREATE TABLE balanceo_carga_semanal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    empleado_id BIGINT NOT NULL,
    semana_inicio DATE NOT NULL,
    cantidad_tareas_pesadas INT NOT NULL DEFAULT 0,
    horas_totales_turno INT NOT NULL DEFAULT 0,
    fecha_creacion DATETIME NOT NULL,
    fecha_actualizacion DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado_en DATETIME NULL,
    eliminado_por BIGINT NULL,
    CONSTRAINT fk_balanceo_empleado FOREIGN KEY (empleado_id) REFERENCES empleado(id)
) ENGINE=InnoDB;

CREATE INDEX idx_balanceo_empleado_semana ON balanceo_carga_semanal(empleado_id, semana_inicio);
