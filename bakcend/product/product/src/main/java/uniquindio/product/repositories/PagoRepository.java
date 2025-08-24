package uniquindio.product.repositories;

import uniquindio.product.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, String> {
    List<Pago> findByTipoPago(String tipoPago);
    List<Pago> findByEstado(String estado);
    Optional<Pago> findByCodigoAutorizacion(String codigoAutorizacion);
    List<Pago> findByValorTransaccionGreaterThan(Double valorMinimo);
}