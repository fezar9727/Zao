package com.zao.backend.api.error;

/** Se lanza cuando un recurso pedido no existe (la API responde 404). */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
