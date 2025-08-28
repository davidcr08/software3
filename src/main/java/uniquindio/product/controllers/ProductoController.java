package uniquindio.product.controllers;

import uniquindio.product.enums.TipoProducto;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.services.interfaces.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        try {
            Producto productoCreado = productoService.crearProducto(producto);
            return ResponseEntity.ok(productoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable String id) {
        try {
            return productoService.obtenerProducto(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Producto>> obtenerProductosPorTipo(@PathVariable TipoProducto tipo) {
        try {
            List<Producto> productos = productoService.obtenerProductosPorTipo(tipo);
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/valor-mayor/{valorMinimo}")
    public ResponseEntity<List<Producto>> obtenerProductosConValorMayorA(@PathVariable Double valorMinimo) {
        try {
            List<Producto> productos = productoService.obtenerProductosConValorMayorA(valorMinimo);
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/valor-menor/{valorMaximo}")
    public ResponseEntity<List<Producto>> obtenerProductosConValorMenorA(@PathVariable Double valorMaximo) {
        try {
            List<Producto> productos = productoService.obtenerProductosConValorMenorA(valorMaximo);
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<Producto> actualizarProducto(@RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarProductos() {
        try {
            List<Producto> productos = productoService.listarProductos();
            return ResponseEntity.ok(productos);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}