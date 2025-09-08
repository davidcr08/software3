package uniquindio.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ImagenDTO;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.services.interfaces.ProductoService;
import uniquindio.product.services.interfaces.ImagesService;
import uniquindio.product.services.interfaces.PedidoService;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductoService productoService;
    private final ImagesService imagesService;
    private final PedidoService pedidoService;

    // ==================== PRODUCTOS ==================== //

    /**
     * Crea un nuevo producto.
     *
     * @param productoDTO DTO con los datos del producto a crear
     * @return ResponseEntity con mensaje de éxito y el ID del producto creado
     */
    @PostMapping("/producto")
    public ResponseEntity<MensajeDTO<String>> crearProducto(@Valid @RequestBody CrearProductoDTO productoDTO)
            throws ProductoException {
        productoService.crearProducto(productoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Producto creado correctamente con ID: "));
    }

    /**
     * Actualiza un producto existente.
     */
    @PutMapping("/producto/{id}")
    public ResponseEntity<MensajeDTO<String>> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody EditarProductoDTO productoDTO
    ) throws ProductoException {
        productoService.actualizarProducto(id, productoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Producto actualizado correctamente."));
    }

    /**
     * Elimina un producto por su ID.
     */
    @DeleteMapping("/eliminar-producto/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarProducto(@PathVariable String id)
            throws ProductoException {
        productoService.eliminarProducto(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Producto eliminado correctamente."));
    }

    // ==================== IMÁGENES ==================== //

    /**
     * Sube una imagen al proveedor configurado (ej. Cloudinary).
     */
    @PostMapping("/imagenes")
    public ResponseEntity<MensajeDTO<ImagenDTO>> subirImagen(
            @RequestParam("imagen") MultipartFile imagen
    ) throws Exception {
        ImagenDTO respuesta = imagesService.subirImagen(imagen);
        return ResponseEntity.ok(new MensajeDTO<>(false, respuesta));
    }

    /**
     * Elimina una imagen por su publicId.
     */
    @DeleteMapping("/imagenes/{idImagen}")
    public ResponseEntity<MensajeDTO<String>> eliminarImagen(
            @PathVariable String idImagen
    ) throws Exception {
        String resultado = imagesService.eliminarImagen(idImagen);
        return ResponseEntity.ok(new MensajeDTO<>(false, resultado));
    }

    //_______________________ENDPOINTS PARA ORDEN_________________________________

    @GetMapping("/pedido/{idPedido}")
    public ResponseEntity<MensajeDTO<MostrarPedidoDTO>> mostrarPedido(
            @PathVariable String idPedido
    ) throws ProductoException, PedidoException {
        MostrarPedidoDTO pedido = pedidoService.mostrarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedido));
    }

    /**
     * Eliminar un pedido (id)
     */
    @DeleteMapping("/eliminar-pedido/{idPedido}")
    public ResponseEntity<MensajeDTO<String>> eliminarPedido(
            @PathVariable String idPedido
    ) throws PedidoException {
        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Pedido eliminado correctamente"));
    }
}