package uniquindio.product.dto.pedido;

import uniquindio.product.model.vo.Pago;

import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        String idPedido,
        String idCliente,
        LocalDate fecha,
        Double total,
        List<MostrarDetallePedidoDTO> detalles,
        Pago pago
) {}