package com.zao.backend.api.service;

import com.zao.backend.api.dto.ActualizarTurnoRequest;
import com.zao.backend.api.dto.CrearTurnoRequest;
import com.zao.backend.api.dto.TurnoRespuesta;
import com.zao.backend.api.error.ConflictoException;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.ChecklistRepository;
import com.zao.backend.api.repository.EmpleadoRepository;
import com.zao.backend.api.repository.EstacionRepository;
import com.zao.backend.api.repository.TurnoRepository;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.tareas.Checklist;
import com.zao.backend.turnos.Estacion;
import com.zao.backend.turnos.Turno;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logica de negocio de turnos: listar, consultar, crear, actualizar y eliminar.
 * Al crear un turno se crea tambien su checklist vacio. La eliminacion es
 * logica: el turno se marca como inactivo y nunca se borra de la base.
 */
@Service
@Profile("servicios")
public class TurnoService {

    private final TurnoRepository turnos;
    private final EmpleadoRepository empleados;
    private final EstacionRepository estaciones;
    private final ChecklistRepository checklists;

    public TurnoService(TurnoRepository turnos, EmpleadoRepository empleados,
                        EstacionRepository estaciones, ChecklistRepository checklists) {
        this.turnos = turnos;
        this.empleados = empleados;
        this.estaciones = estaciones;
        this.checklists = checklists;
    }

    /** Lista los turnos activos; si se indica un restaurante, solo los de ese restaurante. */
    @Transactional(readOnly = true)
    public List<TurnoRespuesta> listar(Long restauranteId) {
        List<Turno> encontrados = (restauranteId == null)
                ? turnos.findByActivoTrue()
                : turnos.findByRestaurante_IdAndActivoTrue(restauranteId);
        return encontrados.stream().map(TurnoRespuesta::desde).toList();
    }

    /** Consulta un turno activo por su id. */
    @Transactional(readOnly = true)
    public TurnoRespuesta consultar(Long id) {
        return TurnoRespuesta.desde(buscarActivo(id));
    }

    /** Crea un turno y su checklist vacio. La estacion debe ser del mismo restaurante que el empleado. */
    @Transactional
    public TurnoRespuesta crear(CrearTurnoRequest solicitud) {
        Empleado empleado = empleados.findByIdAndActivoTrue(solicitud.empleadoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe un empleado activo con id " + solicitud.empleadoId() + "."));
        Estacion estacion = buscarEstacion(solicitud.estacionId());
        verificarMismoRestaurante(estacion, empleado.getRestaurante().getId());

        Turno turno = turnos.save(new Turno(empleado.getRestaurante(), empleado, estacion,
                solicitud.fechaTurno(), solicitud.horaInicio(), solicitud.horaFin(),
                solicitud.esNocturno(), solicitud.esFestivo()));
        checklists.save(new Checklist(turno));
        return TurnoRespuesta.desde(turno);
    }

    /** Actualiza la estacion, la fecha, el horario y las marcas de un turno. */
    @Transactional
    public TurnoRespuesta actualizar(Long id, ActualizarTurnoRequest solicitud) {
        Turno turno = buscarActivo(id);
        Estacion estacion = buscarEstacion(solicitud.estacionId());
        verificarMismoRestaurante(estacion, turno.getRestaurante().getId());
        turno.actualizarDatos(estacion, solicitud.fechaTurno(), solicitud.horaInicio(),
                solicitud.horaFin(), solicitud.esNocturno(), solicitud.esFestivo());
        return TurnoRespuesta.desde(turno);
    }

    /** Elimina logicamente un turno (activo = false). */
    @Transactional
    public void eliminar(Long id) {
        // Sin autenticacion por token todavia no se conoce quien elimina, por eso se pasa null.
        buscarActivo(id).marcarComoEliminado(null);
    }

    private Turno buscarActivo(Long id) {
        return turnos.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un turno activo con id " + id + "."));
    }

    private Estacion buscarEstacion(Long id) {
        return estaciones.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe una estacion activa con id " + id + "."));
    }

    private void verificarMismoRestaurante(Estacion estacion, Long restauranteId) {
        if (!estacion.getRestaurante().getId().equals(restauranteId)) {
            throw new ConflictoException("La estacion no pertenece al restaurante del turno.");
        }
    }
}
