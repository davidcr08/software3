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
    private String idInventario;

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
}