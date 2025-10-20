package uniquindio.product.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.inventario.DetalleLoteDTO;
import uniquindio.product.dto.inventario.ProductoBajoStockDTO;
import uniquindio.product.dto.inventario.ResumenInventarioDTO;
import uniquindio.product.dto.inventario.StockPorLoteDTO;
import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.exceptions.LoteException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.services.interfaces.InventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestión de Inventario", description = "Endpoints para el Encargado de Almacén")
public class EncargadoAlmacenController {

    private final InventarioService inventarioService;

    // ==================== CONSULTAS DE INVENTARIO ==================== //

    @Operation(summary = "Obtener stock disponible de un producto",
            description = "Suma el stock de todos los lotes disponibles y no vencidos del producto")
    @GetMapping("/stock/{idProducto}")
    public ResponseEntity<MensajeDTO<Integer>> obtenerStockDisponible(
            @PathVariable String idProducto
    ) throws ProductoException {
        Integer stock = inventarioService.obtenerStockDisponible(idProducto);
        return ResponseEntity.ok(new MensajeDTO<>(false, stock));
    }

    @Operation(summary = "Obtener resumen completo del inventario",
            description = "Lista todos los productos con su stock total, cantidad de lotes y próximo vencimiento")
    @GetMapping("/resumen")
    public ResponseEntity<MensajeDTO<List<ResumenInventarioDTO>>> obtenerResumenInventario() {
        List<ResumenInventarioDTO> resumen = inventarioService.obtenerResumenInventario();
        return ResponseEntity.ok(new MensajeDTO<>(false, resumen));
    }

    @Operation(summary = "Obtener stock detallado por lote de un producto",
            description = "Muestra cada lote del producto con su cantidad, fecha de vencimiento y estado")
    @GetMapping("/stock-detallado/{idProducto}")
    public ResponseEntity<MensajeDTO<List<StockPorLoteDTO>>> obtenerStockPorLote(
            @PathVariable String idProducto
    ) throws ProductoException {
        List<StockPorLoteDTO> stockDetallado = inventarioService.obtenerStockPorLote(idProducto);
        return ResponseEntity.ok(new MensajeDTO<>(false, stockDetallado));
    }

    @Operation(summary = "Obtener productos con stock bajo",
            description = "Lista productos cuyo stock total es menor al umbral especificado")
    @GetMapping("/alertas/stock-bajo")
    public ResponseEntity<MensajeDTO<List<ProductoBajoStockDTO>>> obtenerProductosBajoStock(
            @RequestParam(defaultValue = "10") int umbral
    ) {
        List<ProductoBajoStockDTO> productosBajoStock = inventarioService.obtenerProductosBajoStock(umbral);
        return ResponseEntity.ok(new MensajeDTO<>(false, productosBajoStock));
    }

    // ==================== OPERACIONES DE ALMACÉN ==================== //

    @Operation(summary = "Registrar entrada de lote al almacén",
            description = "El Encargado de Almacén recibe un lote de producción y lo registra como disponible en el inventario físico")
    @PostMapping("/registrar-entrada/{idLote}")
    public ResponseEntity<MensajeDTO<String>> registrarEntradaAlmacen(
            @PathVariable String idLote
    ) throws LoteException, InventarioException {
        inventarioService.registrarEntradaAlmacen(idLote);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lote registrado en almacén correctamente"));
    }

    @GetMapping("/lotes")
    public ResponseEntity<MensajeDTO<List<DetalleLoteDTO>>> listarLotes() {
        return ResponseEntity.ok(new MensajeDTO<>(false, inventarioService.listarLotes()));
    }

}