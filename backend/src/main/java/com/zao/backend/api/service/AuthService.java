package com.zao.backend.api.service;

import com.zao.backend.api.dto.EmpleadoRespuesta;
import com.zao.backend.api.dto.LoginRequest;
import com.zao.backend.api.dto.RegistroRequest;
import com.zao.backend.api.dto.RespuestaLogin;
import com.zao.backend.api.error.AutenticacionFallidaException;
import com.zao.backend.api.error.ConflictoException;
import com.zao.backend.api.error.RecursoNoEncontradoException;
import com.zao.backend.api.repository.EmpleadoRepository;
import com.zao.backend.api.repository.RestauranteRepository;
import com.zao.backend.identidad.Empleado;
import com.zao.backend.restaurante.Restaurante;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Logica de negocio de autenticacion: registro e inicio de sesion.
 * Las contrasenas se guardan cifradas con BCrypt y nunca en texto plano.
 */
@Service
@Profile("servicios")
public class AuthService {

    private final EmpleadoRepository empleados;
    private final RestauranteRepository restaurantes;
    private final PasswordEncoder codificador;

    public AuthService(EmpleadoRepository empleados,
                       RestauranteRepository restaurantes,
                       PasswordEncoder codificador) {
        this.empleados = empleados;
        this.restaurantes = restaurantes;
        this.codificador = codificador;
    }

    /**
     * Registra un empleado nuevo. Falla con 409 si el correo ya existe y con
     * 404 si el restaurante indicado no existe.
     */
    @Transactional
    public EmpleadoRespuesta registrar(RegistroRequest solicitud) {
        String correo = solicitud.correo().trim().toLowerCase();

        if (empleados.existsByCorreo(correo)) {
            throw new ConflictoException("Ya existe un empleado registrado con ese correo.");
        }

        Restaurante restaurante = restaurantes.findByIdAndActivoTrue(solicitud.restauranteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("El restaurante indicado no existe."));

        Empleado empleado = new Empleado(
                restaurante,
                solicitud.nombreCompleto().trim(),
                correo,
                codificador.encode(solicitud.password()),
                solicitud.rol(),
                solicitud.fechaContratacion());
        // La validacion del DTO ya garantiza que el empleado acepto el tratamiento de datos.
        empleado.aceptarAutorizacionDatos();

        return EmpleadoRespuesta.desde(empleados.save(empleado));
    }

    /**
     * Verifica el correo y la contrasena. Si no coinciden lanza
     * AutenticacionFallidaException con un mensaje generico.
     */
    @Transactional(readOnly = true)
    public RespuestaLogin iniciarSesion(LoginRequest solicitud) {
        Empleado empleado = empleados.findByCorreoAndActivoTrue(solicitud.correo().trim().toLowerCase())
                .orElseThrow(AutenticacionFallidaException::new);

        if (!codificador.matches(solicitud.password(), empleado.getPasswordHash())) {
            throw new AutenticacionFallidaException();
        }

        return new RespuestaLogin(true, "Autenticacion satisfactoria.", EmpleadoRespuesta.desde(empleado));
    }
}
