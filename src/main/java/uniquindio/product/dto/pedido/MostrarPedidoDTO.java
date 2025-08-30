package uniquindio.product.dto.pedido;

import java.time.LocalDate;
import java.util.List;

public record MostrarPedidoDTO(
        String idPedido,
        String nombreCliente,
        LocalDate fechaCompra,
        Double total,
        List<MostrarDetallePedidoDTO> detalles
) {}