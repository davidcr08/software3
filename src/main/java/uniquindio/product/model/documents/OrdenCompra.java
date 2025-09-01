package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.model.enums.FormaPago;

@Getter
@Setter
@Entity
@Table(name = "orden_de_compra")
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "id_cliente", nullable = false)
    private String idCliente;

    @Column(name = "id_seller", nullable = false)
    private String idSeller;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_orden_de_compra")
    private FormaPago estadoOrdenCompra;


    @Column(name = "costo_total")
    private Double costoTotal;


}