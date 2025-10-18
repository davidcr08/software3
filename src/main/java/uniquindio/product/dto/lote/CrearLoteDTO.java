package uniquindio.product.dto.lote;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CrearLoteDTO(

        @NotBlank(message = "El código de lote es obligatorio")
        @Size(max = 50)
        String codigoLote,

        @NotNull(message = "El ID del producto no puede ser nulo")
        String idProducto,

        @NotNull(message = "La fecha de producción es obligatoria")
        @PastOrPresent(message = "La fecha de producción no puede ser futura")
        LocalDate fechaProduccion,

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        @Future(message = "La fecha de vencimiento debe ser futura")
        LocalDate fechaVencimiento,

        @Min(value = 1, message = "Debe producirse al menos 1 unidad")
        Integer cantidadProducida,

        String observaciones
) {}