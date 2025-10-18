package uniquindio.product.controllers;

import com.mercadopago.resources.preference.Preference;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import uniquindio.product.configs.AuthUtils;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.carrito.CarritoDTO;
import uniquindio.product.dto.carrito.CarritoResponseDTO;
import uniquindio.product.dto.carrito.DetalleCarritoDTO;
import uniquindio.product.dto.carrito.InformacionProductoCarritoDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.pedido.PedidoResponseDTO;
import uniquindio.product.dto.usuario.EditarUsuarioDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.services.interfaces.CarritoService;
import uniquindio.product.services.interfaces.PedidoService;
import uniquindio.product.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    //_______________________ENDPOINTS PARA PERFIL_________________________________

    @GetMapping("/perfil")
    public ResponseEntity<MensajeDTO<InformacionUsuarioDTO>> obtenerInformacionUsuario(Authentication authentication) throws UsuarioException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        InformacionUsuarioDTO informacion = usuarioService.obtenerUsuario(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, informacion));
    }

    @PutMapping("/editar")
    public ResponseEntity<MensajeDTO<String>> editarUsuario(
            @Valid @RequestBody EditarUsuarioDTO usuarioDTO,
            Authentication authentication) throws UsuarioException {

        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        usuarioService.editarUsuario(usuarioDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Perfil actualizado correctamente"));
    }

    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<MensajeDTO<String>> eliminarUsuario(Authentication authentication) throws UsuarioException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }

    //_______________________ENDPOINTS PARA CARRITO_________________________________

    @Operation(summary = "Obtener carrito completo del usuario")
    @GetMapping("/mi-carrito")
    public ResponseEntity<MensajeDTO<CarritoResponseDTO>> obtenerCarritoCompleto(
            Authentication authentication) throws CarritoException, ProductoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoResponseDTO carrito = carritoService.obtenerCarritoCompleto(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    @Operation(summary = "Agregar items al carrito")
    @PostMapping("/mi-carrito/items")
    public ResponseEntity<MensajeDTO<CarritoDTO>> agregarItemsAlCarrito(
            Authentication authentication,
            @Valid @RequestBody List<DetalleCarritoDTO> nuevosItems
    ) throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoDTO carrito = carritoService.agregarItemsAlCarrito(id, nuevosItems);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    @Operation(summary = "Eliminar un item del carrito")
    @DeleteMapping("/mi-carrito/items/{idProducto}")
    public ResponseEntity<MensajeDTO<CarritoDTO>> eliminarItemDelCarrito(
            Authentication authentication,
            @PathVariable String idProducto
    ) throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoDTO carrito = carritoService.eliminarItemDelCarrito(id, idProducto);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    @Operation(summary = "Vaciar el carrito")
    @DeleteMapping("/mi-carrito")
    public ResponseEntity<MensajeDTO<CarritoDTO>> vaciarCarrito(
            Authentication authentication) throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoDTO carrito = carritoService.vaciarCarrito(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    @Operation(summary = "Listar productos en el carrito")
    @GetMapping("/mi-carrito/items")
    public ResponseEntity<MensajeDTO<List<InformacionProductoCarritoDTO>>> listarProductosEnCarrito(
            Authentication authentication
    ) throws CarritoException, ProductoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        List<InformacionProductoCarritoDTO> productos = carritoService.listarProductosEnCarrito(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    @Operation(summary = "Calcular total del carrito")
    @GetMapping("/mi-carrito/total")
    public ResponseEntity<MensajeDTO<Double>> calcularTotalCarrito( Authentication authentication) throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        Double total = carritoService.calcularTotalCarrito(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, total));
    }

    //_______________________ENDPOINTS PARA PEDIDOS_________________________________

    @Operation(summary = "Crear pedido desde el carrito")
    @PostMapping("/pedidos")
    public ResponseEntity<MensajeDTO<MostrarPedidoDTO>> crearPedidoDesdeCarrito(
            Authentication authentication)
            throws CarritoException, ProductoException, PedidoException {
        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        MostrarPedidoDTO pedido = pedidoService.crearPedidoDesdeCarrito(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedido));
    }

    @Operation(summary = "Obtener todos mis pedidos")
    @GetMapping("/pedidos")
    public ResponseEntity<MensajeDTO<List<PedidoResponseDTO>>> obtenerMisPedidos(
            Authentication authentication) throws PedidoException {
        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerPedidosPorCliente(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedidos));
    }

    @Operation(summary = "Ver detalle de mi pedido")
    @GetMapping("/pedidos/{idPedido}")
    public ResponseEntity<MensajeDTO<MostrarPedidoDTO>> mostrarMiPedido(
            @PathVariable String idPedido,
            Authentication authentication
    ) throws ProductoException, PedidoException {
        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        MostrarPedidoDTO pedido = pedidoService.mostrarPedido(idPedido);

        // Validar propiedad del pedido
        if (!pedido.nombreCliente().equals(idCliente)) {
            throw new PedidoException("No tienes permiso para ver este pedido");
        }

        return ResponseEntity.ok(new MensajeDTO<>(false, pedido));
    }

    @Operation(summary = "Iniciar proceso de pago")
    @PostMapping("/pedidos/{idPedido}/pago")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(
            @PathVariable String idPedido,
            Authentication authentication
    ) throws Exception {
        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        MostrarPedidoDTO pedido = pedidoService.mostrarPedido(idPedido);

        // Validar propiedad del pedido
        if (!pedido.nombreCliente().equals(idCliente)) {
            throw new PedidoException("No tienes permiso para pagar este pedido");
        }

        Preference preference = pedidoService.realizarPago(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, preference));
    }

    @Operation(summary = "Eliminar mi pedido")
    @DeleteMapping("/pedidos/{idPedido}")
    public ResponseEntity<MensajeDTO<String>> eliminarMiPedido(
            @PathVariable String idPedido,
            Authentication authentication
    ) throws PedidoException, ProductoException {
        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        MostrarPedidoDTO pedido = pedidoService.mostrarPedido(idPedido);

        // Validar propiedad del pedido
        if (!pedido.nombreCliente().equals(idCliente)) {
            throw new PedidoException("No tienes permiso para eliminar este pedido");
        }

        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Pedido eliminado correctamente"));
    }
}