package uniquindio.product.dto.producto;

import uniquindio.product.model.enums.TipoProducto;

public record ItemProductoDTO(
        String idProducto,
        String nombreProducto,
        String imagenProducto,
        String descripcion,
        Double valor,
        TipoProducto tipo
) {}