package uniquindio.product.dto.pqr;

import uniquindio.product.model.enums.CategoriaPqr;
import uniquindio.product.model.enums.EstadoPqr;

import java.time.LocalDateTime;

public record PqrResponseDTO(
        String idPqr,
        String idUsuario,
        CategoriaPqr categoriaPqr,
        String descripcion,
        String idWorker,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaRespuesta,
        EstadoPqr estadoPqr
){}
