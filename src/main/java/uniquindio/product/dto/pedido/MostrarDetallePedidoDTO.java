package uniquindio.product.dto.pedido;

import uniquindio.product.model.enums.TipoProducto;

public record MostrarDetallePedidoDTO(
        String idProducto,
        String nombreProducto,
        TipoProducto tipoProducto,
        String imagenProducto,
        Double precioUnitario,
        int cantidad,
        Double subtotal
) {}