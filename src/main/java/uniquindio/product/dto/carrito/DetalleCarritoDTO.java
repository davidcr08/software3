package uniquindio.product.dto.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleCarritoDTO(
        @NotNull(message = "El ID del producto no puede ser nulo")
        String idProducto,

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        int cantidad
) {}