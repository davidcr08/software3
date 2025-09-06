package uniquindio.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.services.interfaces.ProductoService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AminController {

    private final ProductoService productoService;

    //_______________________ENDPOINTS PARA PRODUCTOS_________________________________
    /**
     * Crea un nuevo producto.
     */
    @PostMapping("/crear-producto")
    public ResponseEntity<MensajeDTO<String>> crearProducto(@Valid @RequestBody CrearProductoDTO productoDTO) {
        try {
            productoService.crearProducto(productoDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Producto creado correctamente."));
        } catch (ProductoException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }
    /**
     * Actualiza un producto por su ID.
     */
    @PutMapping("/actualizar-producto/{id}")
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
