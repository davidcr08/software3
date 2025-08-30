package uniquindio.product.controllers;

import uniquindio.product.dto.carrito.*;
import uniquindio.product.services.interfaces.CarritoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    @PostMapping
    public ResponseEntity<Void> crearCarrito(@Valid @RequestBody CrearCarritoDTO carritoDTO) {
        carritoService.crearCarrito(carritoDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarrito(@PathVariable String idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarritoCompleto(idUsuario));
    }

    @PostMapping("/{idUsuario}/agregar")
    public ResponseEntity<CarritoResponseDTO> agregarItems(
            @PathVariable String idUsuario,
            @Valid @RequestBody List<DetalleCarritoDTO> itemsDTO) {
        carritoService.agregarItemsAlCarrito(idUsuario, itemsDTO);
        return ResponseEntity.ok(carritoService.obtenerCarritoCompleto(idUsuario));
    }

    @DeleteMapping("/{idUsuario}/eliminar/{idProducto}")
    public ResponseEntity<CarritoResponseDTO> eliminarItem(
            @PathVariable String idUsuario,
            @PathVariable String idProducto) {
        carritoService.eliminarItemDelCarrito(idUsuario, idProducto);
        return ResponseEntity.ok(carritoService.obtenerCarritoCompleto(idUsuario));
    }

    @DeleteMapping("/{idUsuario}/vaciar")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable String idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idUsuario}/items")
    public ResponseEntity<List<InformacionProductoCarritoDTO>> listarItems(@PathVariable String idUsuario) {
        return ResponseEntity.ok(carritoService.listarProductosEnCarrito(idUsuario));
    }

    @GetMapping("/{idUsuario}/total")
    public ResponseEntity<Double> obtenerTotal(@PathVariable String idUsuario) {
        return ResponseEntity.ok(carritoService.calcularTotalCarrito(idUsuario));
    }
}