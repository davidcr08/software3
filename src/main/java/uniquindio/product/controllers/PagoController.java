package uniquindio.product.controllers;

import uniquindio.product.model.vo.Pago;
import uniquindio.product.services.interfaces.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<Pago> crearPago(@RequestBody Pago pago) {
        try {
            Pago pagoCreado = pagoService.crearPago(pago);
            return ResponseEntity.ok(pagoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPago(@PathVariable String id) {
        try {
            return pagoService.obtenerPago(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/tipo/{tipoPago}")
    public ResponseEntity<List<Pago>> obtenerPagosPorTipo(@PathVariable String tipoPago) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorTipo(tipoPago);
            return ResponseEntity.ok(pagos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pago>> obtenerPagosPorEstado(@PathVariable String estado) {
        try {
            List<Pago> pagos = pagoService.obtenerPagosPorEstado(estado);
            return ResponseEntity.ok(pagos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/autorizacion/{codigoAutorizacion}")
    public ResponseEntity<Pago> obtenerPagoPorCodigoAutorizacion(@PathVariable String codigoAutorizacion) {
        try {
            return pagoService.obtenerPagoPorCodigoAutorizacion(codigoAutorizacion)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<Pago> actualizarPago(@RequestBody Pago pago) {
        try {
            Pago pagoActualizado = pagoService.actualizarPago(pago);
            return ResponseEntity.ok(pagoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable String id) {
        try {
            pagoService.eliminarPago(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listarPagos() {
        try {
            List<Pago> pagos = pagoService.listarPagos();
            return ResponseEntity.ok(pagos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}