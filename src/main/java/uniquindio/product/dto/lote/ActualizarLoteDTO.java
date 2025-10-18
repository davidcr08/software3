package uniquindio.product.dto.lote;

import jakarta.validation.constraints.*;
import uniquindio.product.model.enums.EstadoLote;

import java.time.LocalDate;

public record ActualizarLoteDTO(

        @Size(max = 50, message = "El código no puede superar 50 caracteres")
        String codigoLote,

        String idProducto,

        @PastOrPresent(message = "La fecha de producción no puede ser futura")
        LocalDate fechaProduccion,

        @Future(message = "La fecha de vencimiento debe ser futura")
        LocalDate fechaVencimiento,

        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer cantidadProducida,

        @Min(value = 0, message = "La cantidad disponible no puede ser negativa")
        Integer cantidadDisponible,

        EstadoLote estado,

        String observaciones,

        @NotBlank(message = "Debe especificar el motivo")
        String motivo
) {}