package uniquindio.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uniquindio.product.model.documents.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, String> {

}