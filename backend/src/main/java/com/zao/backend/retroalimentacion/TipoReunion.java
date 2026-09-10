package com.zao.backend.retroalimentacion;

/**
 * INDIVIDUAL: retroalimentación privada admin-empleado, nunca visible para
 * otros empleados. GRUPAL: briefing operativo o capacitación de equipo, no
 * contiene datos de desempeño individual y puede ser visible para todos los
 * convocados.
 */
public enum TipoReunion {
    INDIVIDUAL,
    GRUPAL
}
