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
import uniquindio.product.enums.EstadoCupon;
import uniquindio.product.enums.TipoCupon;
import uniquindio.product.model.documents.Cupon;
import uniquindio.product.services.interfaces.CuponService;

/**
 * Controlador REST para gestionar recursos de tipo {@link Cupon}.
 * Expone endpoints CRUD y de validación bajo la ruta base "/api/cupones".
 */
@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
public class CuponController {

    private final CuponService cuponService;

    @PostMapping
    /** Crea un nuevo cupón. */
    public ResponseEntity<Cupon> crearCupon(@RequestBody Cupon cupon) {
        try {
            Cupon creado = cuponService.crearCupon(cupon);
            return ResponseEntity.ok(creado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    /** Obtiene un cupón por su identificador. */
    public ResponseEntity<Cupon> obtenerCupon(@PathVariable String id) {
        try {
            return cuponService.obtenerCupon(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/codigo/{codigo}")
    /** Obtiene un cupón por su código único. */
    public ResponseEntity<Cupon> obtenerCuponPorCodigo(@PathVariable String codigo) {
        try {
            return cuponService.obtenerCuponPorCodigo(codigo)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    /** Actualiza un cupón existente. */
    public ResponseEntity<Cupon> actualizarCupon(@RequestBody Cupon cupon) {
        try {
            Cupon actualizado = cuponService.actualizarCupon(cupon);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    /** Elimina un cupón por ID. */
    public ResponseEntity<Void> eliminarCupon(@PathVariable String id) {
        try {
            cuponService.eliminarCupon(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    /** Lista todos los cupones. */
    public ResponseEntity<List<Cupon>> listarCupones() {
        try {
            List<Cupon> cupones = cuponService.listarCupones();
            return ResponseEntity.ok(cupones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/cliente/{cedulaCliente}")
    /** Busca cupones por cédula del cliente. */
    public ResponseEntity<List<Cupon>> buscarCuponesPorCliente(@PathVariable String cedulaCliente) {
        try {
            List<Cupon> cupones = cuponService.buscarCuponesPorCliente(cedulaCliente);
            return ResponseEntity.ok(cupones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/estado/{estado}")
    /** Busca cupones por estado. */
    public ResponseEntity<List<Cupon>> buscarCuponesPorEstado(@PathVariable EstadoCupon estado) {
        try {
            List<Cupon> cupones = cuponService.buscarCuponesPorEstado(estado);
            return ResponseEntity.ok(cupones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/tipo/{tipoCupon}")
    /** Busca cupones por tipo. */
    public ResponseEntity<List<Cupon>> buscarCuponesPorTipo(@PathVariable TipoCupon tipoCupon) {
        try {
            List<Cupon> cupones = cuponService.buscarCuponesPorTipo(tipoCupon);
            return ResponseEntity.ok(cupones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/cliente/{cedulaCliente}/disponibles")
    /** Busca cupones disponibles por cédula del cliente. */
    public ResponseEntity<List<Cupon>> buscarCuponesDisponiblesPorCliente(@PathVariable String cedulaCliente) {
        try {
            List<Cupon> cupones = cuponService.buscarCuponesDisponiblesPorCliente(cedulaCliente);
            return ResponseEntity.ok(cupones);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/validar/{codigo}")
    /** Valida si un cupón está disponible para uso. */
    public ResponseEntity<Boolean> validarCupon(@PathVariable String codigo) {
        try {
            boolean esValido = cuponService.validarCupon(codigo);
            return ResponseEntity.ok(esValido);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }
}
