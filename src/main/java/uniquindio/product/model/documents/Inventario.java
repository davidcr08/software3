package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.*;
import uniquindio.product.model.vo.DetalleInventario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "inventario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Inventario {

    @Id
    @EqualsAndHashCode.Include
    @Column(name = "id_inventario", nullable = false, updatable = false)
    private String idInventario;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "detalle_inventario", joinColumns = @JoinColumn(name = "inventario_id"))
    private List<DetalleInventario> detalleInventario = new ArrayList<>();

    // Buscar detalle por lote específico
    public Optional<DetalleInventario> buscarDetallePorLote(String idLote) {
        return detalleInventario.stream()
                .filter(detalle -> detalle.getIdLote().equals(idLote))
                .findFirst();
    }

    // Buscar todos los lotes de un producto en almacén
    public List<DetalleInventario> buscarDetallesPorProducto(String idProducto) {
        return detalleInventario.stream()
                .filter(detalle -> detalle.getIdProducto().equals(idProducto))
                .toList();
    }

    //Agregar lote al almacén (Encargado registra entrada)
    public void agregarLote(String idLote, String idProducto, Integer cantidad) {
        // Verificar si el lote ya existe en almacén
        if (buscarDetallePorLote(idLote).isPresent()) {
            throw new IllegalStateException("El lote " + idLote + " ya está registrado en el inventario");
        }

        DetalleInventario detalle = new DetalleInventario(
                idLote,
                idProducto,
                cantidad,
                LocalDateTime.now()
        );
        detalleInventario.add(detalle);
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Actualizar cantidad de un lote en almacén (ajustes)
    public void actualizarCantidadLote(String idLote, Integer nuevaCantidad) {
        DetalleInventario detalle = buscarDetallePorLote(idLote)
                .orElseThrow(() -> new IllegalStateException("Lote " + idLote + " no encontrado en inventario"));

        detalle.setCantidad(nuevaCantidad);
        this.ultimaActualizacion = LocalDateTime.now();
    }

    //Reducir cantidad de un lote (al vender)
    public void reducirCantidadLote(String idLote, Integer cantidad) {
        DetalleInventario detalle = buscarDetallePorLote(idLote)
                .orElseThrow(() -> new IllegalStateException("Lote " + idLote + " no encontrado en inventario"));

        if (detalle.getCantidad() < cantidad) {
            throw new IllegalStateException(
                    "Cantidad insuficiente en inventario. Disponible: " + detalle.getCantidad() + ", Requerido: " + cantidad
            );
        }

        detalle.setCantidad(detalle.getCantidad() - cantidad);
        this.ultimaActualizacion = LocalDateTime.now();
    }

    //Eliminar lote del almacén (si cantidad = 0 o lote vencido)
    public void eliminarLote(String idLote) {
        detalleInventario.removeIf(detalle -> detalle.getIdLote().equals(idLote));
        this.ultimaActualizacion = LocalDateTime.now();
    }

    //Obtener stock total de un producto (suma de todos sus lotes)
    public Integer obtenerStockTotalProducto(String idProducto) {
        return buscarDetallesPorProducto(idProducto).stream()
                .mapToInt(DetalleInventario::getCantidad)
                .sum();
    }
}