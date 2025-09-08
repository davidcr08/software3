package uniquindio.product.controllers;

import com.mercadopago.resources.preference.Preference;
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
        return ResponseEntity.ok(new MensajeDTO<>(true, informacion));
    }

    @PutMapping("/editar")
    public ResponseEntity<MensajeDTO<String>> editarUsuario(@RequestBody EditarUsuarioDTO usuarioDTO) {
        try {
            usuarioService.editarUsuario(usuarioDTO);
            return ResponseEntity.ok(new MensajeDTO<>(true, "Perfil actualizado correctamente"));
        } catch (UsuarioException e) {
            return ResponseEntity.badRequest().body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<MensajeDTO<String>> eliminarUsuario(Authentication authentication) throws UsuarioException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }

    //_______________________ENDPOINTS PARA CARRITO_________________________________

    /**
     * Obtiene el carrito completo de un usuario.
     */
    @GetMapping("/mi-carrito")
    public ResponseEntity<MensajeDTO<CarritoResponseDTO>> obtenerCarritoCompleto(Authentication authentication) throws CarritoException, ProductoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoResponseDTO carrito = carritoService.obtenerCarritoCompleto(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Agrega ítems al carrito del usuario autenticado.
     */
    @PostMapping("/mi-carrito/items")
    public ResponseEntity<MensajeDTO<CarritoDTO>> agregarItemsAlCarrito(
            Authentication authentication,
            @RequestBody List<DetalleCarritoDTO> nuevosItems
    ) throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoDTO carrito = carritoService.agregarItemsAlCarrito(id, nuevosItems);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Elimina un ítem del carrito del usuario autenticado.
     */
    @DeleteMapping("/mi-carrito/items/{idProducto}")
    public ResponseEntity<MensajeDTO<CarritoDTO>> eliminarItemDelCarrito(
            Authentication authentication,
            @PathVariable String idProducto
    ) throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoDTO carrito = carritoService.eliminarItemDelCarrito(id, idProducto);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Vacía el carrito del usuario autenticado.
     */
    @DeleteMapping("/mi-carrito/vaciar")
    public ResponseEntity<MensajeDTO<CarritoDTO>> vaciarCarrito(Authentication authentication)
            throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        CarritoDTO carrito = carritoService.vaciarCarrito(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Lista los productos detallados en el carrito del usuario autenticado.
     */
    @GetMapping("/mi-carrito/items")
    public ResponseEntity<MensajeDTO<List<InformacionProductoCarritoDTO>>> listarProductosEnCarrito(
            Authentication authentication
    ) throws CarritoException, ProductoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        List<InformacionProductoCarritoDTO> productos = carritoService.listarProductosEnCarrito(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    /**
     * Calcula el total del carrito del usuario autenticado.
     */
    @GetMapping("/mi-carrito/total")
    public ResponseEntity<MensajeDTO<Double>> calcularTotalCarrito(Authentication authentication)
            throws CarritoException {
        String id = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        Double total = carritoService.calcularTotalCarrito(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, total));
    }

    //_______________________ENDPOINTS PARA ORDEN_________________________________

    /**
     * Crear un pedido desde el carrito del cliente
     */
    @PostMapping("/orden/crear-pedido")
    public ResponseEntity<MensajeDTO<MostrarPedidoDTO>> crearPedidoDesdeCarrito(Authentication authentication)
            throws CarritoException, ProductoException, PedidoException {

        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        MostrarPedidoDTO pedido = pedidoService.crearPedidoDesdeCarrito(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedido));
    }


    /**
     * Obtener todos los pedidos de un cliente
     */
    @GetMapping("/pedido")
    public ResponseEntity<MensajeDTO<List<PedidoResponseDTO>>> obtenerPedidosPorCliente(
            Authentication authentication) throws PedidoException {
        String idCliente = AuthUtils.obtenerIdUsuarioDesdeToken(authentication);
        List<PedidoResponseDTO> pedidos = pedidoService.obtenerPedidosPorCliente(idCliente);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedidos));
    }

    /**
     * Mostrar un pedido en detalle
     */
    @GetMapping("/detalle/{idPedido}")
    public ResponseEntity<MensajeDTO<MostrarPedidoDTO>> mostrarPedido(
            @PathVariable String idPedido
    ) throws ProductoException, PedidoException {
        MostrarPedidoDTO pedido = pedidoService.mostrarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, pedido));
    }

    /**
     * Iniciar proceso de pago con MercadoPago
     */
    @PostMapping("/pago/{idPedido}")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(
            @PathVariable String idPedido
    ) throws Exception {
        Preference preference = pedidoService.realizarPago(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, preference));
    }

    @DeleteMapping("/eliminar-pedido/{idPedido}")
    public ResponseEntity<MensajeDTO<String>> eliminarPedido(
            @PathVariable String idPedido
    ) throws PedidoException {
        pedidoService.eliminarPedido(idPedido);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Pedido eliminado correctamente"));
    }
}