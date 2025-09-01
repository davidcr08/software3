package uniquindio.product.dto.pedido;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CrearPedidoDTO(
        @NotBlank(message = "El ID del cliente no puede estar vacío.")
        String idCliente,

        String codigoPasarela,


        @NotNull(message = "La fecha de la orden es obligatoria.")
        @FutureOrPresent
        LocalDate fecha,

        @NotNull(message = "La lista de detalles del pedido no puede estar vacía.")
        @Size(min = 1, message = "El pedido debe tener al menos un detalle.")
        List<DetallePedidoDTO> detallePedido,

        PagoDTO pago
) {}