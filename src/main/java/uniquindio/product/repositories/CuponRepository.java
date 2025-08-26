package uniquindio.product.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uniquindio.product.enums.EstadoCupon;
import uniquindio.product.enums.TipoCupon;
import uniquindio.product.model.Cupon;

/**
 * Repositorio JPA para la entidad {@link Cupon}.
 * Provee operaciones CRUD básicas y consultas específicas para cupones.
 */
@Repository
public interface CuponRepository extends JpaRepository<Cupon, String> {
    
    /**
     * Busca un cupón por su código único.
     */
    Optional<Cupon> findByCodigo(String codigo);
    
    /**
     * Verifica si existe un cupón con el código especificado.
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Busca cupones por cédula del cliente.
     */
    List<Cupon> findByCedulaCliente(String cedulaCliente);
    
    /**
     * Busca cupones por estado.
     */
    List<Cupon> findByEstado(EstadoCupon estado);
    
    /**
     * Busca cupones por tipo.
     */
    List<Cupon> findByTipoCupon(TipoCupon tipoCupon);
    
    /**
     * Busca cupones disponibles por cédula del cliente.
     */
    List<Cupon> findByCedulaClienteAndEstado(String cedulaCliente, EstadoCupon estado);
}
