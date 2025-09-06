package uniquindio.product.mapper;

import uniquindio.product.dto.carrito.CarritoDTO;
import uniquindio.product.dto.carrito.DetalleCarritoDTO;
import uniquindio.product.model.documents.Carrito;

public class CarritoMapper {

    public static CarritoDTO toDTO(Carrito carrito) {
        return new CarritoDTO(
                carrito.getId(),
                carrito.getUsuario().getId(),
                carrito.getItems().stream()
                        .map(item -> new DetalleCarritoDTO(
                                item.getIdProducto(),
                                item.getCantidad()
                        ))
                        .toList()
        );
    }
}