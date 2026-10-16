package com.sistema.gestion.exception;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author sosap
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Captura errores de validacion (datos faltantes, duplicados, contraseñas cortas)
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> manejarErroresDeNegocio(RuntimeException ex) {
        System.err.println("--> [AVISO DE VALIDACIÓN]: " + ex.getMessage());

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "ERROR_DE_VALIDACION");
        respuesta.put("mensaje", ex.getMessage());
        respuesta.put("fecha", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // Captura cualquier otro error inesperado (ej. se apago MySQL)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception ex) {
        System.err.println("--> [ERROR CRÍTICO DEL SISTEMA]: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("estado", "ERROR_INTERNO");
        respuesta.put("causa", ex.getMessage());
        respuesta.put("pista_para_dev", "Revisa la consola de NetBeans buscando '[ERROR CRÍTICO DEL SISTEMA]'");
        respuesta.put("fecha", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
