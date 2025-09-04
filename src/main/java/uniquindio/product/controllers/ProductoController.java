package uniquindio.product.controllers;

import uniquindio.product.dto.autenticacion.MensajeDTO;
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

    /**
     * Crea un nuevo producto.
     */
    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crearProducto(@Valid @RequestBody CrearProductoDTO productoDTO) {
        try {
            productoService.crearProducto(productoDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Producto creado correctamente."));
        } catch (ProductoException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    /**
     * Lista todos los productos.
     */
    @GetMapping
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> listarProductos() throws ProductoException {
        List<ItemProductoDTO> productos = productoService.listarProductos();
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    /**
     * Obtiene productos por tipo.
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> obtenerProductosPorTipo(@PathVariable TipoProducto tipo) throws ProductoException {
        List<ItemProductoDTO> productos = productoService.obtenerProductosPorTipo(tipo);
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    /**
     * Obtiene un producto por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDTO<ProductoDetalleDTO>> obtenerProducto(@PathVariable String id) throws ProductoException {
        ProductoDetalleDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, producto));
    }

    /**
     * Actualiza un producto por su ID.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody EditarProductoDTO productoDTO) {
        try {
            productoService.actualizarProducto(id, productoDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Producto actualizado correctamente."));
        } catch (ProductoException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    /**
     * Elimina un producto por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarProducto(@PathVariable String id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Producto eliminado correctamente."));
        } catch (ProductoException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

}