package com.zao.backend.api.service;

import com.zao.backend.api.dto.ActualizarEmpleadoRequest;
import com.zao.backend.api.dto.EmpleadoRespuesta;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.EmpleadoRepository;
import com.zao.backend.identidad.Empleado;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logica de negocio de empleados: consultar, actualizar y eliminar.
 * La creacion se hace con el registro (AuthService). La eliminacion es
 * logica: el registro se marca como inactivo y nunca se borra de la base.
 */
@Service
@Profile("servicios")
public class EmpleadoService {

    private final EmpleadoRepository empleados;

    public EmpleadoService(EmpleadoRepository empleados) {
        this.empleados = empleados;
    }

    /** Lista los empleados activos; si se indica un restaurante, solo los de ese restaurante. */
    @Transactional(readOnly = true)
    public List<EmpleadoRespuesta> listar(Long restauranteId) {
        List<Empleado> encontrados = (restauranteId == null)
                ? empleados.findByActivoTrue()
                : empleados.findByRestaurante_IdAndActivoTrue(restauranteId);
        return encontrados.stream().map(EmpleadoRespuesta::desde).toList();
    }

    /** Consulta un empleado activo por su id. */
    @Transactional(readOnly = true)
    public EmpleadoRespuesta consultar(Long id) {
        return EmpleadoRespuesta.desde(buscarActivo(id));
    }

    /** Actualiza el nombre y el rol de un empleado. */
    @Transactional
    public EmpleadoRespuesta actualizar(Long id, ActualizarEmpleadoRequest solicitud) {
        Empleado empleado = buscarActivo(id);
        empleado.setNombreCompleto(solicitud.nombreCompleto().trim());
        empleado.setRol(solicitud.rol());
        return EmpleadoRespuesta.desde(empleado);
    }

    /** Elimina logicamente a un empleado (activo = false). */
    @Transactional
    public void eliminar(Long id) {
        // Sin autenticacion por token todavia no se conoce quien elimina, por eso se pasa null.
        buscarActivo(id).marcarComoEliminado(null);
    }

    private Empleado buscarActivo(Long id) {
        return empleados.findByIdAndActivoTrue(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un empleado activo con id " + id + "."));
    }
}
