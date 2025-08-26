package uniquindio.product.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.enums.TipoWorker;

@Getter
@Setter
@Entity
@Table(name = "worker")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_worker", nullable = false)
    private TipoWorker tipoWorker;

    @Column(name = "seller_id", unique = true)
    private String sellerId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "numero_contrato", unique = true)
    private String numeroContrato;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "contrasena")
    private String contrasena;
}