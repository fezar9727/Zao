package com.zao.backend.api.error;

/** Se lanza cuando la operacion choca con datos existentes (la API responde 409). */
public class ConflictoException extends RuntimeException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
