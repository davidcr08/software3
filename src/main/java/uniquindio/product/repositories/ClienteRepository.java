package uniquindio.product.repositories;

import uniquindio.product.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByCedula(String cedula);
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
    boolean existsByCedula(String cedula);
    boolean existsByCorreoElectronico(String correoElectronico);
}