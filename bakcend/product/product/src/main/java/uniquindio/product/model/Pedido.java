package uniquindio.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Embeddable
public class Pedido {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedidoId")
    private List<Producto> productos = new ArrayList<>();

    @Column(name = "costo")
    private Double costo;

}
