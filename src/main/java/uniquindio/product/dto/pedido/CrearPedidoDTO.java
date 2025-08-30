package uniquindio.product.dto.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record CrearPedidoDTO(
        @NotBlank(message = "El ID del cliente no puede estar vacío.")
        String idCliente,

        String codigoPasarela,

        @NotNull(message = "La lista de detalles del pedido no puede estar vacía.")
        @Size(min = 1, message = "El pedido debe tener al menos un detalle.")
        List<DetallePedidoDTO> detallePedido
) {}