package uniquindio.product.controllers;

import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;
import uniquindio.product.model.enums.TipoProducto;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.services.interfaces.ProductoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ProductoDetalleDTO> crearProducto(@Valid @RequestBody CrearProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.crearProducto(productoDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDetalleDTO> obtenerProducto(@PathVariable String id) throws ProductoException {
        return ResponseEntity.ok(productoService.obtenerProducto(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDetalleDTO> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody EditarProductoDTO productoDTO) throws ProductoException {
        return ResponseEntity.ok(productoService.actualizarProducto(id, productoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable String id) throws ProductoException {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ItemProductoDTO>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ItemProductoDTO>> obtenerProductosPorTipo(@PathVariable TipoProducto tipo) {
        return ResponseEntity.ok(productoService.obtenerProductosPorTipo(tipo));
    }
}