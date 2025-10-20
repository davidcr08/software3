package uniquindio.product.dto.inventario;

import uniquindio.product.model.enums.EstadoLote;

import java.time.LocalDate;

public record DetalleLoteDTO(
        String idLote,
        String idProducto,
        String nombreProducto,
        int cantidadDisponible,
        LocalDate fechaVencimiento,
        EstadoLote estado
) {}
