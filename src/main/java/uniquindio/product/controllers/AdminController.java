package uniquindio.product.controllers;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.pedido.MostrarPedidoDTO;
import uniquindio.product.dto.usuario.CrearTrabajadorDTO;
import uniquindio.product.dto.usuario.InformacionUsuarioDTO;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.UsuarioException;
import uniquindio.product.services.interfaces.PedidoService;
import uniquindio.product.services.interfaces.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

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

    // ==================== GESTIÓN DE TRABAJADORES ==================== //

    /**
     * Crea un nuevo trabajador (empleado) en el sistema.
     * Solo accesible por ADMINISTRADOR.
     * La cuenta se crea directamente ACTIVA sin necesidad de validación por email.
     *
     * @param trabajadorDTO datos del trabajador a crear
     * @return ResponseEntity con mensaje de confirmación
     * @throws UsuarioException si ya existe usuario con email o cédula
     */
    @Operation(
            summary = "Crear nuevo trabajador",
            description = "Permite al administrador crear cuentas de empleados (GESTOR_PRODUCTOS, SUPERVISOR_PRODUCCION, ENCARGADO_ALMACEN, ADMINISTRADOR). La cuenta se crea directamente activa sin carrito asociado."
    )
    @PostMapping("/trabajadores")
    public ResponseEntity<MensajeDTO<String>> crearTrabajador(
            @Valid @RequestBody CrearTrabajadorDTO trabajadorDTO
    ) throws UsuarioException {
        usuarioService.crearTrabajador(trabajadorDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Trabajador creado correctamente"));
    }

    /**
     * Lista todos los trabajadores (usuarios con roles diferentes a CLIENTE).
     *
     * @return ResponseEntity con lista de trabajadores
     */
    @Operation(summary = "Listar todos los trabajadores")
    @GetMapping("/trabajadores")
    public ResponseEntity<MensajeDTO<List<InformacionUsuarioDTO>>> listarTrabajadores() throws UsuarioException {
        List<InformacionUsuarioDTO> trabajadores = usuarioService.listarTrabajadores();
        return ResponseEntity.ok(new MensajeDTO<>(false, trabajadores));
    }

    /**
     * Obtiene la información de un trabajador específico.
     *
     * @param idTrabajador ID del trabajador
     * @return ResponseEntity con información del trabajador
     */
    @Operation(summary = "Obtener información de un trabajador")
    @GetMapping("/trabajadores/{idTrabajador}")
    public ResponseEntity<MensajeDTO<InformacionUsuarioDTO>> obtenerTrabajador(
            @PathVariable String idTrabajador
    ) throws UsuarioException {
        InformacionUsuarioDTO trabajador = usuarioService.obtenerUsuario(idTrabajador);
        return ResponseEntity.ok(new MensajeDTO<>(false, trabajador));
    }

    /**
     * Desactiva (elimina lógicamente) la cuenta de un trabajador.
     *
     * @param idTrabajador ID del trabajador a desactivar
     * @return ResponseEntity con mensaje de confirmación
     */
    @Operation(summary = "Desactivar cuenta de trabajador")
    @DeleteMapping("/trabajadores/{idTrabajador}")
    public ResponseEntity<MensajeDTO<String>> desactivarTrabajador(
            @PathVariable String idTrabajador
    ) throws UsuarioException {
        usuarioService.eliminarUsuario(idTrabajador);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Trabajador desactivado correctamente"));
    }
}