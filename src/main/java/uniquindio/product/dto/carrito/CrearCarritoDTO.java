package uniquindio.product.dto.carrito;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CrearCarritoDTO(
        @NotBlank(message = "El id del usuario no puede ser nulo o vacío")
        String idUsuario,

        @NotEmpty(message = "Los items del carrito no pueden ser nulos ni vacíos")
        List<DetalleCarritoDTO> itemsCarrito
) {}