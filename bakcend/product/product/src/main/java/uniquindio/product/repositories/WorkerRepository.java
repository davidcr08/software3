package uniquindio.product.repositories;

import uniquindio.product.enums.TipoWorker;
import uniquindio.product.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, String> {
    Optional<Worker> findBySellerId(String sellerId);
    Optional<Worker> findByNumeroContrato(String numeroContrato);
    Optional<Worker> findByEmail(String email);
    List<Worker> findByTipoWorker(TipoWorker tipoWorker);
    List<Worker> findByActivo(Boolean activo);
    boolean existsBySellerId(String sellerId);
    boolean existsByNumeroContrato(String numeroContrato);
    boolean existsByEmail(String email);
}