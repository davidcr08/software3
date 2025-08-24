package uniquindio.product.repositories;

import uniquindio.product.model.GuiaEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GuiaEntregaRepository extends JpaRepository<GuiaEntrega, String> {
    List<GuiaEntrega> findByIdOrdenCompra(String idOrdenCompra);
    List<GuiaEntrega> findByEstadoGuia(String estadoGuia);
}