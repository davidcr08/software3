package uniquindio.product.controllers;

import org.springframework.security.core.Authentication;
import uniquindio.product.configs.AuthUtils;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.carrito.CarritoDTO;
import uniquindio.product.dto.carrito.CarritoResponseDTO;
import uniquindio.product.dto.carrito.DetalleCarritoDTO;
import uniquindio.product.dto.carrito.InformacionProductoCarritoDTO;
import uniquindio.product.dto.usuario.EditarUsuarioDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.services.interfaces.CarritoService;
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
    @GetMapping("/{idUsuario}")
    public ResponseEntity<MensajeDTO<CarritoResponseDTO>> obtenerCarritoCompleto(@PathVariable String idUsuario)
            throws CarritoException, ProductoException {
        CarritoResponseDTO carrito = carritoService.obtenerCarritoCompleto(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Agrega ítems al carrito del usuario.
     */
    @PostMapping("/{idUsuario}/items")
    public ResponseEntity<MensajeDTO<CarritoDTO>> agregarItemsAlCarrito(
            @PathVariable String idUsuario,
            @RequestBody List<DetalleCarritoDTO> nuevosItems
    ) throws CarritoException {
        CarritoDTO carrito = carritoService.agregarItemsAlCarrito(idUsuario, nuevosItems);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Elimina un ítem del carrito del usuario.
     */
    @DeleteMapping("/{idUsuario}/items/{idProducto}")
    public ResponseEntity<MensajeDTO<CarritoDTO>> eliminarItemDelCarrito(
            @PathVariable String idUsuario,
            @PathVariable String idProducto
    ) throws CarritoException {
        CarritoDTO carrito = carritoService.eliminarItemDelCarrito(idUsuario, idProducto);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Vacía el carrito de un usuario.
     */
    @DeleteMapping("/{idUsuario}/vaciar")
    public ResponseEntity<MensajeDTO<CarritoDTO>> vaciarCarrito(@PathVariable String idUsuario)
            throws CarritoException {
        CarritoDTO carrito = carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, carrito));
    }

    /**
     * Lista los productos detallados en el carrito del usuario.
     */
    @GetMapping("/{idUsuario}/items")
    public ResponseEntity<MensajeDTO<List<InformacionProductoCarritoDTO>>> listarProductosEnCarrito(
            @PathVariable String idUsuario
    ) throws CarritoException, ProductoException {
        List<InformacionProductoCarritoDTO> productos = carritoService.listarProductosEnCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, productos));
    }

    /**
     * Calcula el total del carrito (valor monetario).
     */
    @GetMapping("/{idUsuario}/total")
    public ResponseEntity<MensajeDTO<Double>> calcularTotalCarrito(@PathVariable String idUsuario)
            throws CarritoException {
        Double total = carritoService.calcularTotalCarrito(idUsuario);
        return ResponseEntity.ok(new MensajeDTO<>(false, total));
    }
}