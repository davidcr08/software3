package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.*;
import uniquindio.product.model.vo.DetalleCarrito;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Carrito {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private String id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "carrito_items",
            joinColumns = @JoinColumn(name = "carrito_id"),
            foreignKey = @ForeignKey(name = "FK_carrito_items_carrito")
    )
    private List<DetalleCarrito> items = new ArrayList<>();

    public void agregarOActualizarItem(DetalleCarrito nuevoItem) {
        for (DetalleCarrito item : items) {
            if (item.getIdProducto().equals(nuevoItem.getIdProducto())) {
                item.setCantidad(item.getCantidad() + nuevoItem.getCantidad());
                return;
            }
        }
        items.add(nuevoItem);
    }

    public boolean eliminarItem(String idProducto) {
        return items.removeIf(item -> item.getIdProducto().equals(idProducto));
    }

    // Vaciar carrito
    public void vaciar() {
        if (items != null) {
            items.clear();
        }
    }

    // Calcular total del carrito (se necesitaría obtener valor del producto desde el servicio)
    public int cantidadTotal() {
        return items.stream()
                .mapToInt(DetalleCarrito::getCantidad)
                .sum();
    }
}