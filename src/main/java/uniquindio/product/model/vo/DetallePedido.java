package uniquindio.product.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Column(name = "id_producto", nullable = false)
    private String idProducto;

    @Column(name = "cantidad", nullable = false)
    @NotNull
    @Positive
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal precioUnitario;
}
