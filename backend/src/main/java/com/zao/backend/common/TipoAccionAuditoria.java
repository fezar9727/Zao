package com.zao.backend.common;

/**
 * Tipos de acción que la bitácora global de auditoría puede registrar.
 * No incluye un valor "DELETE" físico porque el sistema nunca borra filas;
 * ELIMINACION_LOGICA representa el marcado de una entidad como inactiva.
 */
public enum TipoAccionAuditoria {
    CREACION,
    MODIFICACION,
    ELIMINACION_LOGICA,
    INICIO_SESION,
    CIERRE_SESION_TURNO
}
