package uniquindio.product.model.vo;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uniquindio.product.enums.EstadoPago;
import uniquindio.product.enums.Moneda;
import uniquindio.product.enums.TipoPago;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    private String idPago;
    private Moneda moneda;
    private TipoPago tipoPago;
    private String detalleEstado;
    private String codigoAutorizacion;
    private LocalDateTime fecha;
    private Double valorTransaccion;
    private EstadoPago estado;
    private String metodoPago;
}