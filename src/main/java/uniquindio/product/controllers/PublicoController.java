package uniquindio.product.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.enums.TipoProducto;
import uniquindio.product.services.interfaces.PedidoService;
import uniquindio.product.services.interfaces.ProductoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publico")
@RequiredArgsConstructor
public class PublicoController {

    private final ProductoService productoService;
    private final PedidoService pedidoService;

    //_______________________Webhook de MercadoPago_________________________________

    @PostMapping("/pagos/notificacion")
    public ResponseEntity<MensajeDTO<String>> recibirNotificacion(@RequestBody Map<String, Object> request) {
        pedidoService.recibirNotificacionMercadoPago(request);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Notificación recibida correctamente"));
    }

    //_______________________ENDPOINTS PARA PRODUCTOS_________________________________
    /**
     * Lista todos los productos.
     */
    @GetMapping("/productos")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> listarProductos() throws ProductoException {
        List<ItemProductoDTO> productos = productoService.listarProductos();
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    /**
     * Obtiene productos por tipo.
     */
    @GetMapping("/productos/{tipo}")
    public ResponseEntity<MensajeDTO<List<ItemProductoDTO>>> obtenerProductosPorTipo(@PathVariable TipoProducto tipo) throws ProductoException {
        List<ItemProductoDTO> productos = productoService.obtenerProductosPorTipo(tipo);
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    /**
     * Obtiene un producto por su ID.
     */
    @GetMapping("/productos/{id}")
    public ResponseEntity<MensajeDTO<ProductoDetalleDTO>> obtenerProducto(@PathVariable String id) throws ProductoException {
        ProductoDetalleDTO producto = productoService.obtenerProductoPorId(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, producto));
    }
}