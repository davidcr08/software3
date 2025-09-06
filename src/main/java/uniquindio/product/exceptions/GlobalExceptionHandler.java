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

    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<MensajeDTO<String>> handleCuentaException(UsuarioException ex) {
        return ResponseEntity.badRequest().body(new MensajeDTO<>(false, ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MensajeDTO<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MensajeDTO<>(false, "Error interno del servidor: " + ex.getMessage()));
    }

}