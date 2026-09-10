package com.zao.backend.turnos;

/**
 * Estado del ciclo de vida de un turno. Un turno solo puede pasar a
 * CERRADO si su checklist asociado está completo, o si quedó con
 * pendientes justificados (ver módulo de auditoría) — nunca se cierra
 * un turno con tareas críticas sin marcar ni justificar.
 */
public enum EstadoTurno {
    PROGRAMADO,
    EN_CURSO,
    CERRADO
}
