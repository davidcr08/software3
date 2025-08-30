package uniquindio.product.controllers;

import uniquindio.product.dto.pedido.*;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.services.interfaces.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/desde-carrito/{idCliente}")
    public ResponseEntity<PedidoResponseDTO> crearPedidoDesdeCarrito(
            @PathVariable String idCliente,
            @RequestParam(required = false) String codigoPasarela) {
        return ResponseEntity.ok(convertirAPedidoResponseDTO(pedidoService.crearPedidoDesdeCarrito(idCliente, codigoPasarela)));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody CrearPedidoDTO pedidoDTO) {
        return ResponseEntity.ok(convertirAPedidoResponseDTO(pedidoService.crearPedido(pedidoDTO)));
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<MostrarPedidoDTO> mostrarPedido(@PathVariable String idPedido) {
        return ResponseEntity.ok(pedidoService.mostrarPedido(idPedido));
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorCliente(@PathVariable String idCliente) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorCliente(idCliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable String id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }

    private PedidoResponseDTO convertirAPedidoResponseDTO(Pedido pedido) {
        List<MostrarDetallePedidoDTO> detalles = new ArrayList<>();


        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getIdCliente(),
                pedido.getFecha(),
                pedido.getTotal(),
                detalles
        );
    }
}