package uniquindio.product.model.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pago")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "moneda")
    private String moneda;

    @Column(name = "tipoPago")
    private String tipoPago;

    @Column(name = "detalleEstado")
    private String detalleEstado;

    @Column(name = "codigoAutorizacion")
    private String codigoAutorizacion;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "valorTransaccion")
    private Double valorTransaccion;

    @Column(name = "estado")
    private String estado;

    @Column(name = "metodoPago")
    private String metodoPago;


}