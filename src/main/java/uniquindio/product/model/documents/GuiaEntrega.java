package uniquindio.product.model.documents;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.enums.EstadoGuiaEntrega;

@Getter
@Setter
@Entity
@Table(name = "guiaEntrega")
public class GuiaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoGuia")
    private EstadoGuiaEntrega estadoGuia;

    @Column(name = "idOrdenCompra")
    private String idOrdenCompra;
}