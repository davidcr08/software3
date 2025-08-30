package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.*;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.model.vo.Pago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pedido {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_cliente", nullable = false)
    private String idCliente;

    @Column(name = "codigo_pasarela")
    private String codigoPasarela;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDate fecha;

    @Embedded
    private Pago pago;

    @ElementCollection
    @CollectionTable(name = "pedido_detalles", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<DetallePedido> detalle = new ArrayList<>();

    @Column(name = "total", nullable = false)
    private Double total;
}