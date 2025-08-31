

package uniquindio.product.dto.pedido;

import uniquindio.product.enums.EstadoPago;
import uniquindio.product.enums.Moneda;
import uniquindio.product.enums.TipoPago;

import java.time.LocalDateTime;

public record PagoDTO(
        String idPago,
        Moneda moneda,
        TipoPago tipoPago,
        String detalleEstado,
        String codigoAutorizacion,
        LocalDateTime fecha,
        Double valorTransaccion,
        EstadoPago estado,
        String metodoPago
) {}