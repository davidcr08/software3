package uniquindio.product.controllers;

import uniquindio.product.model.GuiaEntrega;
import uniquindio.product.services.interfaces.GuiaEntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/guias-entrega")
@RequiredArgsConstructor
public class GuiaEntregaController {

    private final GuiaEntregaService guiaEntregaService;

    @PostMapping
    public ResponseEntity<GuiaEntrega> crearGuiaEntrega(@RequestBody GuiaEntrega guiaEntrega) {
        try {
            GuiaEntrega guiaCreada = guiaEntregaService.crearGuiaEntrega(guiaEntrega);
            return ResponseEntity.ok(guiaCreada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuiaEntrega> obtenerGuia(@PathVariable String id) {
        try {
            return guiaEntregaService.obtenerGuia(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/orden-compra/{idOrdenCompra}")
    public ResponseEntity<List<GuiaEntrega>> obtenerGuiasPorOrdenCompra(@PathVariable String idOrdenCompra) {
        try {
            List<GuiaEntrega> guias = guiaEntregaService.obtenerGuiasPorOrdenCompra(idOrdenCompra);
            return ResponseEntity.ok(guias);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<GuiaEntrega>> obtenerGuiasPorEstado(@PathVariable String estado) {
        try {
            List<GuiaEntrega> guias = guiaEntregaService.obtenerGuiasPorEstado(estado);
            return ResponseEntity.ok(guias);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<GuiaEntrega> actualizarGuia(@RequestBody GuiaEntrega guiaEntrega) {
        try {
            GuiaEntrega guiaActualizada = guiaEntregaService.actualizarGuia(guiaEntrega);
            return ResponseEntity.ok(guiaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarGuia(@PathVariable String id) {
        try {
            guiaEntregaService.eliminarGuia(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GuiaEntrega>> listarGuias() {
        try {
            List<GuiaEntrega> guias = guiaEntregaService.listarGuias();
            return ResponseEntity.ok(guias);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}