package uniquindio.product.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<MensajeDTO<String>> handleGenericException(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new MensajeDTO<>(true, "Error interno del servidor: " + ex.getMessage()));
    }

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<MensajeDTO<String>> handleCuentaException(UsuarioException ex) {
        return ResponseEntity.badRequest().body(new MensajeDTO<>(true, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeDTO<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeDTO<>(true, "Error interno del servidor: " + ex.getMessage()));
    }

}