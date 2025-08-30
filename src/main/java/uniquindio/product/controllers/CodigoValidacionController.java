package uniquindio.product.controllers;

import uniquindio.product.model.vo.CodigoValidacion;
import uniquindio.product.services.interfaces.CodigoValidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/codigos-validacion")
@RequiredArgsConstructor
public class CodigoValidacionController {

    private final CodigoValidacionService codigoValidacionService;

    @PostMapping
    public ResponseEntity<CodigoValidacion> crearCodigo(@RequestBody CodigoValidacion codigoValidacion) {
        try {
            CodigoValidacion codigoCreado = codigoValidacionService.crearCodigo(codigoValidacion);
            return ResponseEntity.ok(codigoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodigoValidacion> obtenerCodigo(@PathVariable Long id) {
        try {
            return codigoValidacionService.obtenerCodigo(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CodigoValidacion> obtenerCodigoPorCodigo(@PathVariable String codigo) {
        try {
            return codigoValidacionService.obtenerCodigoPorCodigo(codigo)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/validar/{codigo}")
    public ResponseEntity<Boolean> validarCodigo(@PathVariable String codigo) {
        try {
            boolean esValido = codigoValidacionService.validarCodigo(codigo);
            return ResponseEntity.ok(esValido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCodigo(@PathVariable Long id) {
        try {
            codigoValidacionService.eliminarCodigo(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}