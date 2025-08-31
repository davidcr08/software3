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
    private String id;

    @Column(name = "id_usuario", nullable = false, unique = true)
    private String idUsuario;

    @ElementCollection(fetch = FetchType.EAGER) // Cambia a EAGER
    @CollectionTable(
            name = "carrito_items",
            joinColumns = @JoinColumn(name = "carrito_id"),
            foreignKey = @ForeignKey(name = "FK_carrito_items_carrito")
    )
    @Column(name = "items") // Asegura el mapeo
    private List<DetalleCarrito> items = new ArrayList<>();

    // Metodo helper para manejar la colección
    public void agregarItem(DetalleCarrito item) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
    }

    public void removerItem(DetalleCarrito item) {
        if (this.items != null) {
            this.items.remove(item);
        }
    }
}