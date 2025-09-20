package uniquindio.product.dto.pedido;

import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.Moneda;
import uniquindio.product.model.enums.TipoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record PagoDTO(
        String idPago,
        Moneda moneda,
        TipoPago tipoPago,
        String detalleEstado,
        String codigoAutorizacion,
        OffsetDateTime fecha,
        BigDecimal valorTransaccion,
        EstadoPago estado,
        String metodoPago
) {}