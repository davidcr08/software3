package uniquindio.product.services.interfaces;

import java.util.List;
import java.util.Optional;

import uniquindio.product.enums.EstadoCupon;
import uniquindio.product.enums.TipoCupon;
import uniquindio.product.model.Cupon;

/**
 * Define las operaciones del dominio para gestionar entidades de tipo {@link Cupon}.
 */
public interface CuponService {
    
    /** Crea un nuevo cupón. */
    Cupon crearCupon(Cupon cupon);
    
    /** Obtiene un cupón por su identificador. */
    Optional<Cupon> obtenerCupon(String id);
    
    /** Obtiene un cupón por su código único. */
    Optional<Cupon> obtenerCuponPorCodigo(String codigo);
    
    /** Actualiza los datos de un cupón existente. */
    Cupon actualizarCupon(Cupon cupon);
    
    /** Elimina un cupón por ID. */
    void eliminarCupon(String id);
    
    /** Lista todos los cupones. */
    List<Cupon> listarCupones();
    
    /** Busca cupones por cédula del cliente. */
    List<Cupon> buscarCuponesPorCliente(String cedulaCliente);
    
    /** Busca cupones por estado. */
    List<Cupon> buscarCuponesPorEstado(EstadoCupon estado);
    
    /** Busca cupones por tipo. */
    List<Cupon> buscarCuponesPorTipo(TipoCupon tipoCupon);
    
    /** Busca cupones disponibles por cédula del cliente. */
    List<Cupon> buscarCuponesDisponiblesPorCliente(String cedulaCliente);
    
    /** Valida si un cupón está disponible para uso. */
    boolean validarCupon(String codigo);
}
