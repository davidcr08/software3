package uniquindio.product.mapper;

import uniquindio.product.dto.lote.ActualizarLoteDTO;
import uniquindio.product.dto.lote.CrearLoteDTO;
import uniquindio.product.dto.lote.MostrarLoteDTO;
import uniquindio.product.model.documents.Lote;

import java.util.Objects;

public final class LoteMapper {

    private LoteMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Lote toEntity(CrearLoteDTO dto) {
        Objects.requireNonNull(dto, "El DTO no puede ser nulo");

        Lote lote = new Lote();
        lote.setCodigoLote(dto.codigoLote());
        lote.setIdProducto(dto.idProducto());
        lote.setFechaProduccion(dto.fechaProduccion());
        lote.setFechaVencimiento(dto.fechaVencimiento());
        lote.setCantidadProducida(dto.cantidadProducida());
        lote.setObservaciones(dto.observaciones());

        return lote;
    }

    public static MostrarLoteDTO toMostrarLoteDTO(Lote lote, String nombreProducto) {
        Objects.requireNonNull(lote, "El lote no puede ser nulo");

        return new MostrarLoteDTO(
                lote.getId(),
                lote.getCodigoLote(),
                lote.getIdProducto(),
                nombreProducto,
                lote.getFechaProduccion(),
                lote.getFechaVencimiento(),
                lote.getCantidadProducida(),
                lote.getCantidadDisponible(),
                lote.getCantidadProducida() - lote.getCantidadDisponible(), // cantidadVendida
                lote.getEstado(),
                lote.getObservaciones(),
                lote.diasParaVencer() < 60, // proximoAVencer
                lote.diasParaVencer()
        );
    }

    public static void actualizarEntidad(Lote lote, ActualizarLoteDTO dto) {
        Objects.requireNonNull(lote, "El lote no puede ser nulo");
        Objects.requireNonNull(dto, "El DTO no puede ser nulo");

        // Actualización parcial: solo actualiza los campos no nulos
        if (dto.codigoLote() != null) {
            lote.setCodigoLote(dto.codigoLote());
        }
        if (dto.idProducto() != null) {
            lote.setIdProducto(dto.idProducto());
        }
        if (dto.fechaProduccion() != null) {
            lote.setFechaProduccion(dto.fechaProduccion());
        }
        if (dto.fechaVencimiento() != null) {
            lote.setFechaVencimiento(dto.fechaVencimiento());
        }
        if (dto.cantidadProducida() != null) {
            lote.setCantidadProducida(dto.cantidadProducida());
        }
        if (dto.cantidadDisponible() != null) {
            lote.setCantidadDisponible(dto.cantidadDisponible());
        }
        if (dto.estado() != null) {
            lote.setEstado(dto.estado());
        }
        if (dto.observaciones() != null) {
            lote.setObservaciones(dto.observaciones());
        }
    }
}