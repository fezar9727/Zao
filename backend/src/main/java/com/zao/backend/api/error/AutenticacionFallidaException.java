package com.zao.backend.api.error;

/**
 * Se lanza cuando el usuario o la contrasena no son correctos (la API responde 401).
 * El mensaje es generico a proposito: no revela cual de los dos datos fallo.
 */
public class AutenticacionFallidaException extends RuntimeException {

    public AutenticacionFallidaException() {
        super("Usuario o contrasena incorrectos.");
    }
}
