package uniquindio.product.model.documents;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.model.vo.Pago;
import uniquindio.product.model.enums.EstadoPedido;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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
    @NotNull
    private String idCliente;

    @Column(name = "codigo_pasarela")
    private String codigoPasarela;

    @Column(name = "fecha_creacion", nullable = false)
    @NotNull
    private OffsetDateTime fechaCreacion;

    @Embedded
    private Pago pago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido")
    private EstadoPedido estado;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "pedido_detalles", joinColumns = @JoinColumn(name = "pedido_id"))
    private List<DetallePedido> detalle = new ArrayList<>();

    @Column(name = "total", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal total;

    public BigDecimal calcularTotal() {
        return detalle.stream()
                .map(d -> d.getPrecioUnitario().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}