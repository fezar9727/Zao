-- Tabla append-only: en Fase 2 se revocan permisos UPDATE y DELETE sobre
-- esta tabla al usuario de aplicación de la base de datos, dejando solo
-- INSERT y SELECT, para reforzar a nivel de motor de base de datos (no solo
-- a nivel de código Java) que estos registros nunca se alteran.
CREATE TABLE bitacora_auditoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurante_id BIGINT NOT NULL,
    usuario_id BIGINT NULL,
    nombre_entidad VARCHAR(100) NOT NULL,
    entidad_id BIGINT NOT NULL,
    tipo_accion VARCHAR(30) NOT NULL,
    valor_anterior LONGTEXT,
    valor_nuevo LONGTEXT,
    fecha_evento DATETIME NOT NULL
) ENGINE=InnoDB;

CREATE INDEX idx_bitacora_restaurante ON bitacora_auditoria(restaurante_id);
CREATE INDEX idx_bitacora_entidad ON bitacora_auditoria(nombre_entidad, entidad_id);
CREATE INDEX idx_bitacora_fecha ON bitacora_auditoria(fecha_evento);
