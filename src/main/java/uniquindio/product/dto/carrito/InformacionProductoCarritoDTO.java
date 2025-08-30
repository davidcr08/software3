package uniquindio.product.dto.carrito;

public record InformacionProductoCarritoDTO(
        DetalleCarritoDTO detalleCarritoDTO,
        String imagenProducto,
        String nombre,
        Double valorUnitario,
        Double subtotal
) {}