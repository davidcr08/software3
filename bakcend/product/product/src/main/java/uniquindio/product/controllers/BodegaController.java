package uniquindio.product.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import uniquindio.product.model.Bodega;
import uniquindio.product.services.interfaces.BodegaService;

/**
 * Controlador REST para gestionar recursos de tipo {@link Bodega}.
 * Expone endpoints CRUD bajo la ruta base "/api/bodegas".
 */
@RestController
@RequestMapping("/api/bodegas")
@RequiredArgsConstructor
public class BodegaController {

    private final BodegaService bodegaService;

    @PostMapping
    /** Crea una nueva bodega. */
    public ResponseEntity<Bodega> crearBodega(@RequestBody Bodega bodega) {
        try {
            Bodega creada = bodegaService.crearBodega(bodega);
            return ResponseEntity.ok(creada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    /** Obtiene una bodega por su identificador. */
    public ResponseEntity<Bodega> obtenerBodega(@PathVariable String id) {
        try {
            return bodegaService.obtenerBodega(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    /** Actualiza una bodega existente. */
    public ResponseEntity<Bodega> actualizarBodega(@RequestBody Bodega bodega) {
        try {
            Bodega actualizada = bodegaService.actualizarBodega(bodega);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    /** Elimina una bodega por ID. */
    public ResponseEntity<Void> eliminarBodega(@PathVariable String id) {
        try {
            bodegaService.eliminarBodega(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    /** Lista todas las bodegas. */
    public ResponseEntity<List<Bodega>> listarBodegas() {
        try {
            List<Bodega> bodegas = bodegaService.listarBodegas();
            return ResponseEntity.ok(bodegas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


