package uniquindio.product.dto.producto;

import java.time.LocalDateTime;
import uniquindio.product.model.enums.TipoProducto;

public record ProductoDetalleDTO(
        String idProducto,
        String nombreProducto,
        String imagenProducto,
        String descripcion,
        LocalDateTime ultimaFechaModificacion,
        Double valor,
        TipoProducto tipo
) {}