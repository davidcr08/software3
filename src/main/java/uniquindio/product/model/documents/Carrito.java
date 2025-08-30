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

    @ElementCollection
    @CollectionTable(name = "carrito_items", joinColumns = @JoinColumn(name = "carrito_id"))
    private List<DetalleCarrito> items = new ArrayList<>();
}