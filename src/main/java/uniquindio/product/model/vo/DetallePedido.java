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
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Column(name = "id_producto", nullable = false, length = 36)
    private String idProducto;

    @Column(name = "id_lote", nullable = false, length = 36)
    private String idLote;

    @Column(name = "cantidad", nullable = false)
    @NotNull
    @Positive
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false, precision = 19, scale = 4)
    @NotNull
    private BigDecimal precioUnitario;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetallePedido that)) return false;
        return Objects.equals(idProducto, that.idProducto) &&
                Objects.equals(idLote, that.idLote);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto, idLote);
    }
}
