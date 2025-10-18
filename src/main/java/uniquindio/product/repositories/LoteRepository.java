package uniquindio.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.enums.EstadoLote;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoteRepository extends JpaRepository<Lote, String> {

    // Verificar si existe código de lote
    boolean existsByCodigoLote(String codigoLote);
    // Buscar por estado
    List<Lote> findByEstado(EstadoLote estado);
    // Buscar lotes que vencen antes de cierta fecha
    List<Lote> findByFechaVencimientoBefore(LocalDate fecha);
    // Lotes disponibles
    List<Lote> findByEstadoAndCantidadDisponibleGreaterThan(EstadoLote estado, Integer cantidad);
    List<Lote> findByIdProducto(String idProducto);
    List<Lote> findByIdProductoAndEstadoAndFechaVencimientoAfter(String idProducto, EstadoLote estado, LocalDate fecha);
    List<Lote> findByEstadoAndFechaVencimientoBeforeAndCantidadDisponibleGreaterThan(
            EstadoLote estado,
            LocalDate fecha,
            Integer cantidad
    );

    // Filtra TODO en la BD
    List<Lote> findByIdProductoAndEstadoAndCantidadDisponibleGreaterThanAndFechaVencimientoAfterOrderByFechaVencimientoAsc(
            String idProducto,
            EstadoLote estado,
            Integer cantidad,
            LocalDate fecha
    );

    List<Lote> findByEstadoAndCantidadDisponibleGreaterThanAndFechaVencimientoAfter(
            EstadoLote estado,
            Integer cantidad,
            LocalDate fecha
    );


}