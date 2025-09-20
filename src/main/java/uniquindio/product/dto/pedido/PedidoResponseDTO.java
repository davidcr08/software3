package uniquindio.product.dto.pedido;

import uniquindio.product.model.vo.Pago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record PedidoResponseDTO(
        String idPedido,
        String idCliente,
        OffsetDateTime fecha,
        BigDecimal total,
        List<MostrarDetallePedidoDTO> detalles,
        Pago pago
) {}