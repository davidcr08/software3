package uniquindio.product.dto.carrito;

import java.util.List;

public record CarritoDTO (
        String idCarrito,
        String idUsuario,
        List<DetalleCarritoDTO> items
){}
