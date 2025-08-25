package uniquindio.product.services.interfaces;

import java.util.List;
import java.util.Optional;

import uniquindio.product.model.Bodega;

/**
 * Define las operaciones del dominio para gestionar entidades de tipo {@link Bodega}.
 */
public interface BodegaService {
    /** Crea una nueva bodega. */
    Bodega crearBodega(Bodega bodega);
    /** Obtiene una bodega por su identificador. */
    Optional<Bodega> obtenerBodega(String id);
    /** Actualiza los datos de una bodega existente. */
    Bodega actualizarBodega(Bodega bodega);
    /** Elimina (o da de baja) una bodega por ID. */
    void eliminarBodega(String id);
    /** Lista todas las bodegas. */
    List<Bodega> listarBodegas();
}


