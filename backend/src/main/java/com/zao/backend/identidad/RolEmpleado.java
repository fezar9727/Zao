package com.zao.backend.identidad;

/**
 * Roles jerárquicos del sistema. Ningún rol tiene acceso total por defecto;
 * los permisos concretos de cada rol se implementan en Fase 2 a nivel de
 * autorización de Spring Security, no acá.
 */
public enum RolEmpleado {
    ADMINISTRADOR,
    CHEF_EJECUTIVO,
    COCINERO_LINEA,
    PERSONAL_SALON,
    CAJERO
}
