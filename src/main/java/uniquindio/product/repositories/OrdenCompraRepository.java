package uniquindio.product.repositories;

import uniquindio.product.model.documents.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, String> {
    List<OrdenCompra> findByIdCliente(String idCliente);
    List<OrdenCompra> findByIdSeller(String idSeller);
    List<OrdenCompra> findByEstadoOrdenCompra(String estadoOrdenCompra);
    List<OrdenCompra> findByCostoTotalGreaterThan(Double costoMinimo);
}