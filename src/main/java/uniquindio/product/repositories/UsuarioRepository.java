package uniquindio.product.repositories;

import uniquindio.product.model.documents.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByCedula(String cedula);
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    boolean existsByCedula(String cedula);
    boolean existsByCorreoElectronico(String correoElectronico);
    Optional<Usuario> findByCodigoVerificacionContrasenia(String codigoVerificacion);
}