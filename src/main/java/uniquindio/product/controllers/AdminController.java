package uniquindio.product.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.pedido.PedidoResponseDTO;
import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.EditarProductoDTO;
import uniquindio.product.dto.producto.ImagenDTO;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.model.enums.EstadoPedido;
import uniquindio.product.services.interfaces.ProductoService;
import uniquindio.product.services.interfaces.ImagesService;
import uniquindio.product.services.interfaces.PedidoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductoService productoService;
    private final ImagesService imagesService;
    private final PedidoService pedidoService;

    // ==================== GESTIÓN DE PEDIDOS ==================== //

    @Operation(summary = "Ver cualquier pedido")
    @GetMapping("/pedidos/{idPedido}")
    public ResponseEntity<MensajeDTO<MostrarPedidoDTO>> mostrarPedido(
            @PathVariable String idPedido
    ) throws ProductoException, PedidoException {
        MostrarPedidoDTO pedido = pedidoService.mostrarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedido));
    }

    @Operation(summary = "Eliminar cualquier pedido")
    @DeleteMapping("/pedidos/{idPedido}")
    public ResponseEntity<MensajeDTO<String>> eliminarPedido(
            @PathVariable String idPedido
    ) throws PedidoException {
        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Pedido eliminado correctamente"));
    }
}