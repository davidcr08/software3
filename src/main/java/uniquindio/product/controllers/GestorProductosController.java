package uniquindio.product.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ImagenDTO;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.services.interfaces.ImagesService;
import uniquindio.product.services.interfaces.ProductoService;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestión de Productos", description = "Endpoints para CRUD de productos (Gestor de Productos y Administrador)")
public class GestorProductosController {

    private final ProductoService productoService;
    private final ImagesService imagesService;

    @Operation(summary = "Crear nuevo producto")
    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crearProducto(@Valid @RequestBody CrearProductoDTO productoDTO)
            throws ProductoException {
        productoService.crearProducto(productoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Producto creado correctamente con ID: "));
    }

    @Operation(summary = "Actualizar producto existente")
    @PutMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody EditarProductoDTO productoDTO
    ) throws ProductoException {
        productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Producto actualizado correctamente"));
    }

    @Operation(summary = "Eliminar producto")
    @DeleteMapping("/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarProducto(@PathVariable String id)
            throws ProductoException {
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Producto eliminado correctamente"));
    }

// ==================== GESTIÓN DE IMÁGENES ==================== //

    @Operation(summary = "Subir imagen de producto")
    @PostMapping("/imagenes")
    public ResponseEntity<MensajeDTO<ImagenDTO>> subirImagen(
            @RequestParam("imagen") MultipartFile imagen
    ) throws Exception {
        ImagenDTO respuesta = imagesService.subirImagen(imagen);
        return ResponseEntity.ok(new MensajeDTO<>(false, respuesta));
    }

    @Operation(summary = "Eliminar imagen de producto")
    @DeleteMapping("/imagenes/{idImagen}")
    public ResponseEntity<MensajeDTO<String>> eliminarImagen(
            @PathVariable String idImagen
    ) throws Exception {
        String resultado = imagesService.eliminarImagen(idImagen);
        return ResponseEntity.ok(new MensajeDTO<>(false, resultado));
    }
}