package uniquindio.product.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.lote.ActualizarLoteDTO;
import uniquindio.product.dto.lote.CrearLoteDTO;
import uniquindio.product.dto.lote.LotePorVencerDTO;
import uniquindio.product.dto.lote.MostrarLoteDTO;
import uniquindio.product.exceptions.LoteException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.enums.EstadoLote;
import uniquindio.product.services.interfaces.LoteService;

import java.util.List;

@RestController
@RequestMapping("/api/lotes")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestión de Lotes", description = "Endpoints para el Supervisor de Producción")
public class SupervisorProduccionController {

    private final LoteService loteService;

    // ==================== CRUD DE LOTES ==================== //

    @Operation(summary = "Crear nuevo lote de producción",
            description = "El Supervisor registra un nuevo lote con estado EN_PRODUCCION")
    @PostMapping
    public ResponseEntity<MensajeDTO<String>> crearLote(
            @Valid @RequestBody CrearLoteDTO dto
    ) throws LoteException, ProductoException {
        String idLote = loteService.crearLote(dto);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lote creado correctamente con ID: " + idLote));
    }

    @Operation(summary = "Obtener detalle de un lote")
    @GetMapping("/{idLote}")
    public ResponseEntity<MensajeDTO<MostrarLoteDTO>> obtenerLote(
            @PathVariable String idLote
    ) throws LoteException {
        MostrarLoteDTO lote = loteService.obtenerLote(idLote);
        return ResponseEntity.ok(new MensajeDTO<>(false, lote));
    }

    @Operation(summary = "Actualizar lote",
            description = "Permite corregir datos del lote. Requiere especificar motivo para auditoría")
    @PutMapping("/{idLote}")
    public ResponseEntity<MensajeDTO<MostrarLoteDTO>> actualizarLote(
            @PathVariable String idLote,
            @Valid @RequestBody ActualizarLoteDTO dto
    ) throws LoteException, ProductoException {
        MostrarLoteDTO loteActualizado = loteService.actualizarLote(idLote, dto);
        return ResponseEntity.ok(new MensajeDTO<>(false, loteActualizado));
    }

    @Operation(summary = "Eliminar lote",
            description = "Solo permite eliminar lotes sin ventas asociadas y que no estén DISPONIBLES")
    @DeleteMapping("/{idLote}")
    public ResponseEntity<MensajeDTO<String>> eliminarLote(
            @PathVariable String idLote
    ) throws LoteException {
        loteService.eliminarLote(idLote);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lote eliminado correctamente"));
    }

    // ==================== GESTIÓN DE ESTADO ==================== //

    @Operation(summary = "Bloquear lote",
            description = "Bloquea un lote impidiendo su venta. Útil para control de calidad o problemas detectados")
    @PatchMapping("/{idLote}/bloquear")
    public ResponseEntity<MensajeDTO<String>> bloquearLote(
            @PathVariable String idLote,
            @RequestParam String motivo
    ) throws LoteException {
        loteService.bloquearLote(idLote, motivo);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lote bloqueado correctamente"));
    }

    @Operation(summary = "Desbloquear lote",
            description = "Desbloquea un lote previamente bloqueado, permitiendo su venta nuevamente")
    @PatchMapping("/{idLote}/desbloquear")
    public ResponseEntity<MensajeDTO<String>> desbloquearLote(
            @PathVariable String idLote
    ) throws LoteException {
        loteService.desbloquearLote(idLote);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Lote desbloqueado correctamente"));
    }

    // ==================== CONSULTAS Y REPORTES ==================== //

    @Operation(summary = "Listar todos los lotes de un producto")
    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<MensajeDTO<List<MostrarLoteDTO>>> listarLotesPorProducto(
            @PathVariable String idProducto
    ) throws ProductoException {
        List<MostrarLoteDTO> lotes = loteService.listarLotesPorProducto(idProducto);
        return ResponseEntity.ok(new MensajeDTO<>(false, lotes));
    }

    @Operation(summary = "Listar lotes disponibles",
            description = "Lista todos los lotes con estado DISPONIBLE y cantidad > 0")
    @GetMapping("/disponibles")
    public ResponseEntity<MensajeDTO<List<MostrarLoteDTO>>> listarLotesDisponibles() {
        List<MostrarLoteDTO> lotes = loteService.listarLotesDisponibles();
        return ResponseEntity.ok(new MensajeDTO<>(false, lotes));
    }

    @Operation(summary = "Obtener lotes próximos a vencer",
            description = "Lista lotes que vencen dentro del umbral de días especificado")
    @GetMapping("/alertas/por-vencer")
    public ResponseEntity<MensajeDTO<List<LotePorVencerDTO>>> obtenerLotesPorVencer(
            @RequestParam(defaultValue = "60") int diasUmbral
    ) {
        List<LotePorVencerDTO> lotes = loteService.obtenerLotesPorVencer(diasUmbral);
        return ResponseEntity.ok(new MensajeDTO<>(false, lotes));
    }

    @Operation(summary = "Listar lotes por estado",
            description = "Filtra lotes por su estado: EN_PRODUCCION, DISPONIBLE, AGOTADO, BLOQUEADO")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<MensajeDTO<List<MostrarLoteDTO>>> listarLotesPorEstado(
            @PathVariable EstadoLote estado
    ) {
        List<MostrarLoteDTO> lotes = loteService.listarLotesPorEstado(estado);
        return ResponseEntity.ok(new MensajeDTO<>(false, lotes));
    }
}