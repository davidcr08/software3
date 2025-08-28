package uniquindio.product.repositories;

import uniquindio.product.model.vo.CodigoValidacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CodigoValidacionRepository extends JpaRepository<CodigoValidacion, Long> {
    Optional<CodigoValidacion> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}