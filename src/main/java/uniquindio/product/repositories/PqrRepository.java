package uniquindio.product.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uniquindio.product.model.documents.PQR;
@Repository
public interface PqrRepository extends JpaRepository<PQR,String> {
    PQR findByIdPqr(String idPqr);
    PQR findByIdWorker (String idWorker);
    PQR findByIdUsuario (String idUsuario);
}
