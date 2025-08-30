package uniquindio.product.dto.carrito;

import java.util.List;

public record CarritoResponseDTO(
        String idCarrito,
        String idUsuario,
        List<InformacionProductoCarritoDTO> items,
        Double total
) {}