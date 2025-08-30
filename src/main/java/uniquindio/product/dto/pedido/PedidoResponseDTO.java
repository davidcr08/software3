package uniquindio.product.dto.pedido;

import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        String idPedido,
        String idCliente,
        LocalDate fecha,
        Double total,
        List<MostrarDetallePedidoDTO> detalles
) {}