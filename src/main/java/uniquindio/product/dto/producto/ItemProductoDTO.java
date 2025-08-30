package uniquindio.product.dto.producto;

import uniquindio.product.enums.TipoProducto;

public record ItemProductoDTO(
        String idProducto,
        String imagenProducto,
        Integer cantidad,
        Double valor,
        TipoProducto tipo
) {}