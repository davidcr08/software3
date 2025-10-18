package uniquindio.product.dto.pedido;

import uniquindio.product.model.enums.TipoProducto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MostrarDetallePedidoDTO(
        String idProducto,
        String nombreProducto,
        TipoProducto tipoProducto,
        String imagenProducto,
        BigDecimal precioUnitario,
        Integer cantidad,
        BigDecimal subtotal,
        String idLote,
        String codigoLote,
        LocalDate fechaVencimiento
) {}