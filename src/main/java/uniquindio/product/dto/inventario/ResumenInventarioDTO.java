package uniquindio.product.dto.inventario;

import java.time.LocalDate;

public record ResumenInventarioDTO(
        String idProducto,
        String nombreProducto,
        Integer stockTotal,
        Integer lotesDisponibles,
        LocalDate proximoVencimiento
) {}