package uniquindio.product.dto.inventario;

public record ProductoBajoStockDTO(
        String idProducto,
        String nombreProducto,
        Integer stockTotal,
        Integer umbral
) {}