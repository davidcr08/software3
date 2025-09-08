package uniquindio.product.model.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.Moneda;
import uniquindio.product.model.enums.TipoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private String idPago;

    @Enumerated(EnumType.STRING)
    private Moneda moneda;

    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    private String detalleEstado;
    private String codigoAutorizacion;

    @Column(name = "fecha_pago")
    private OffsetDateTime fecha;

    @Column(name = "valor_transaccion", precision = 19, scale = 4)
    private BigDecimal valorTransaccion;

    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    private String metodoPago;
}
