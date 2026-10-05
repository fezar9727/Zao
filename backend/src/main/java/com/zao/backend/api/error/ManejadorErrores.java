package com.zao.backend.api.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador global de errores de los servicios web.
 * Convierte cada excepcion en una respuesta JSON con el codigo HTTP correcto
 * y un mensaje claro, sin exponer detalles internos del servidor.
 */
@RestControllerAdvice
@Profile("servicios")
public class ManejadorErrores {

    private static final Logger LOG = LoggerFactory.getLogger(ManejadorErrores.class);

    /** Arma el cuerpo JSON estandar de error: exito=false y un mensaje. */
    private static Map<String, Object> cuerpo(String mensaje) {
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("exito", false);
        respuesta.put("mensaje", mensaje);
        return respuesta;
    }

    /** 400: algun campo del cuerpo no cumple las validaciones. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> datosInvalidos(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.putIfAbsent(error.getField(), error.getDefaultMessage());
        }
        Map<String, Object> respuesta = cuerpo("Datos invalidos.");
        respuesta.put("errores", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    /** 400: el cuerpo no es un JSON valido o trae un valor que no corresponde (por ejemplo un rol inexistente). */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> cuerpoIlegible(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(cuerpo("El cuerpo de la peticion esta ausente o no es un JSON valido."));
    }

    /** 400: un parametro de la ruta no tiene el tipo esperado (por ejemplo un id no numerico). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> parametroInvalido(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(cuerpo("El parametro '" + ex.getName() + "' no tiene un valor valido."));
    }

    /** 401: usuario o contrasena incorrectos. */
    @ExceptionHandler(AutenticacionFallidaException.class)
    public ResponseEntity<Map<String, Object>> autenticacionFallida(AutenticacionFallidaException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(cuerpo(ex.getMessage()));
    }

    /** 404: el recurso no existe. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo(ex.getMessage()));
    }

    /** 409: el dato ya existe (por ejemplo, un correo repetido). */
    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<Map<String, Object>> conflicto(ConflictoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo(ex.getMessage()));
    }

    /** 409: la base de datos rechazo la operacion por una restriccion de integridad. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad(DataIntegrityViolationException ex) {
        LOG.warn("Violacion de integridad de datos", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(cuerpo("La operacion viola una restriccion de los datos."));
    }

    /**
     * Cualquier otro error. Los errores propios de Spring MVC (ruta inexistente,
     * metodo no permitido) conservan su codigo; el resto responde 500 con un
     * mensaje generico y el detalle queda solo en el registro del servidor.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> inesperado(Exception ex) {
        if (ex instanceof ErrorResponse errorSpring) {
            return ResponseEntity.status(errorSpring.getStatusCode())
                    .body(cuerpo("La solicitud no se pudo procesar."));
        }
        LOG.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(cuerpo("Error interno del servidor."));
    }
}
