package uniquindio.product.dto.producto;

import java.time.LocalDateTime;
import uniquindio.product.enums.TipoProducto;

public record ProductoDetalleDTO(
        String idProducto,
        String imagenProducto,
        Integer cantidad,
        LocalDateTime ultimaFechaModificacion,
        Double valor,
        TipoProducto tipo
) {}