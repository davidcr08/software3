package uniquindio.product.dto.producto;

import uniquindio.product.model.enums.TipoProducto;

public record ItemProductoDTO(
        String idProducto,
        String nombreProducto,
        String imagenProducto,
        Integer cantidad,
        Double valor,
        TipoProducto tipo
) {}