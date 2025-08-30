package uniquindio.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uniquindio.product.model.documents.Bodega;

/**
 * Repositorio JPA para la entidad {@link Bodega}.
 * Provee operaciones CRUD básicas mediante {@link JpaRepository}.
 */
@Repository
public interface BodegaRepository extends JpaRepository<Bodega, String> {
}


