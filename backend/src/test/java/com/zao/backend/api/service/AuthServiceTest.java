package com.zao.backend.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
import com.zao.backend.identidad.RolEmpleado;
import com.zao.backend.restaurante.Restaurante;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Pruebas unitarias del modulo de autenticacion (registro e inicio de sesion). */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private EmpleadoRepository empleados;

    @Mock
    private RestauranteRepository restaurantes;

    @Mock
    private PasswordEncoder codificador;

    @InjectMocks
    private AuthService servicio;

    private Restaurante restaurante() {
        return new Restaurante("Zao Centro", "900123456", "Calle 1 # 2-3", "Medellin");
    }

    private Empleado empleadoConHash(String hash) {
        return new Empleado(restaurante(), "Ana Perez", "ana@zao.com", hash,
                RolEmpleado.COCINERO_LINEA, LocalDate.of(2026, 1, 15));
    }

    private RegistroRequest solicitudRegistro() {
        return new RegistroRequest("  Ana Perez  ", "  Ana@Zao.com ", "Clave12345",
                RolEmpleado.COCINERO_LINEA, 1L, LocalDate.of(2026, 1, 15), true);
    }

    @Test
    void registrar_conDatosValidos_normalizaCorreoCifraPasswordYGuarda() {
        when(empleados.existsByCorreo("ana@zao.com")).thenReturn(false);
        when(restaurantes.findByIdAndActivoTrue(1L)).thenReturn(Optional.of(restaurante()));
        when(codificador.encode("Clave12345")).thenReturn("HASH_BCRYPT");
        when(empleados.save(any(Empleado.class))).thenAnswer(inv -> inv.getArgument(0));

        EmpleadoRespuesta respuesta = servicio.registrar(solicitudRegistro());

        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleados).save(captor.capture());
        Empleado guardado = captor.getValue();
        assertEquals("ana@zao.com", guardado.getCorreo());
        assertEquals("Ana Perez", guardado.getNombreCompleto());
        assertEquals("HASH_BCRYPT", guardado.getPasswordHash());
        assertNotEquals("Clave12345", guardado.getPasswordHash());
        assertTrue(guardado.isAutorizacionDatosAceptada());
        assertEquals("ana@zao.com", respuesta.correo());
        assertEquals(RolEmpleado.COCINERO_LINEA, respuesta.rol());
    }

    @Test
    void registrar_conCorreoRepetido_lanzaConflictoYNoGuarda() {
        when(empleados.existsByCorreo("ana@zao.com")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> servicio.registrar(solicitudRegistro()));

        verify(empleados, never()).save(any());
        verify(codificador, never()).encode(any());
        verifyNoInteractions(restaurantes);
    }

    @Test
    void registrar_conRestauranteInexistente_lanzaNoEncontradoYNoGuarda() {
        when(empleados.existsByCorreo("ana@zao.com")).thenReturn(false);
        when(restaurantes.findByIdAndActivoTrue(1L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> servicio.registrar(solicitudRegistro()));

        verify(empleados, never()).save(any());
    }

    @Test
    void iniciarSesion_conCredencialesCorrectas_devuelveExito() {
        when(empleados.findByCorreoAndActivoTrue("ana@zao.com"))
                .thenReturn(Optional.of(empleadoConHash("HASH_BCRYPT")));
        when(codificador.matches("Clave12345", "HASH_BCRYPT")).thenReturn(true);

        RespuestaLogin respuesta = servicio.iniciarSesion(new LoginRequest("  ANA@zao.com ", "Clave12345"));

        assertTrue(respuesta.exito());
        assertEquals("ana@zao.com", respuesta.empleado().correo());
    }

    @Test
    void iniciarSesion_conCorreoDesconocido_lanzaAutenticacionFallidaSinVerificarPassword() {
        when(empleados.findByCorreoAndActivoTrue("nadie@zao.com")).thenReturn(Optional.empty());

        assertThrows(AutenticacionFallidaException.class,
                () -> servicio.iniciarSesion(new LoginRequest("nadie@zao.com", "Clave12345")));

        verifyNoInteractions(codificador);
    }

    @Test
    void iniciarSesion_conPasswordIncorrecta_lanzaAutenticacionFallida() {
        when(empleados.findByCorreoAndActivoTrue("ana@zao.com"))
                .thenReturn(Optional.of(empleadoConHash("HASH_BCRYPT")));
        when(codificador.matches("incorrecta", "HASH_BCRYPT")).thenReturn(false);

        assertThrows(AutenticacionFallidaException.class,
                () -> servicio.iniciarSesion(new LoginRequest("ana@zao.com", "incorrecta")));
    }
}
