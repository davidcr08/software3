package uniquindio.product.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCarrito {

    @Column(name = "id_producto", nullable = false)
    private String idProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    // Sobrescribe equals y hashCode para mejor manejo de colecciones
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetalleCarrito that = (DetalleCarrito) o;
        return Objects.equals(idProducto, that.idProducto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProducto);
    }
}