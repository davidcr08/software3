package uniquindio.product.repositories;

import uniquindio.product.model.documents.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, String> {
    Optional<Carrito> findByIdUsuario(String idUsuario);
    boolean existsByIdUsuario(String idUsuario);
}