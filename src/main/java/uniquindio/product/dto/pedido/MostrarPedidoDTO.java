package uniquindio.product.dto.pedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record MostrarPedidoDTO(
        String idPedido,
        String nombreCliente,
        OffsetDateTime fechaCompra,
        BigDecimal total,
        List<MostrarDetallePedidoDTO> detalles
) {}