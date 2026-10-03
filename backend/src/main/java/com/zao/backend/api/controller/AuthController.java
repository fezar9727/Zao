package com.zao.backend.api.controller;

import com.zao.backend.api.dto.EmpleadoRespuesta;
import com.zao.backend.api.dto.LoginRequest;
import com.zao.backend.api.dto.RegistroRequest;
import com.zao.backend.api.dto.RespuestaLogin;
import com.zao.backend.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Servicio web de autenticacion: registro e inicio de sesion. */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Profile("servicios")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** POST /api/auth/registro: crea un empleado y responde 201. */
    @PostMapping("/registro")
    public ResponseEntity<EmpleadoRespuesta> registrar(@Valid @RequestBody RegistroRequest solicitud) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registrar(solicitud));
    }

    /** POST /api/auth/login: responde 200 si las credenciales son correctas y 401 si no. */
    @PostMapping("/login")
    public ResponseEntity<RespuestaLogin> iniciarSesion(@Valid @RequestBody LoginRequest solicitud) {
        return ResponseEntity.ok(authService.iniciarSesion(solicitud));
    }
}
