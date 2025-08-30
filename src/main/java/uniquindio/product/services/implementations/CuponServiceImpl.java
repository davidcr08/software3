package uniquindio.product.services.implementations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import uniquindio.product.enums.EstadoCupon;
import uniquindio.product.enums.TipoCupon;
import uniquindio.product.model.documents.Cupon;
import uniquindio.product.repositories.CuponRepository;
import uniquindio.product.services.interfaces.CuponService;

/**
 * Implementación de {@link CuponService} usando un {@link CuponRepository} JPA.
 */
@Service
@RequiredArgsConstructor
public class CuponServiceImpl implements CuponService {

    private final CuponRepository cuponRepository;

    @Override
    /**
     * Persiste un nuevo cupón en base de datos.
     * Establece el estado inicial como DISPONIBLE y la fecha de apertura.
     */
    public Cupon crearCupon(Cupon cupon) {
        if (cuponRepository.existsByCodigo(cupon.getCodigo())) {
            throw new RuntimeException("Ya existe un cupón con ese código");
        }
        
        cupon.setEstado(EstadoCupon.DISPONIBLE);
        cupon.setFechaApertura(new Date());
        
        return cuponRepository.save(cupon);
    }

    @Override
    /**
     * Busca un cupón por ID. Lanza excepción si no existe.
     */
    public Optional<Cupon> obtenerCupon(String id) {
        if (!cuponRepository.existsById(id)) {
            throw new RuntimeException("No existe un cupón con ese ID");
        }
        return cuponRepository.findById(id);
    }

    @Override
    /**
     * Busca un cupón por código único.
     */
    public Optional<Cupon> obtenerCuponPorCodigo(String codigo) {
        return cuponRepository.findByCodigo(codigo);
    }

    @Override
    /**
     * Actualiza un cupón existente, validando que el ID exista.
     */
    public Cupon actualizarCupon(Cupon cupon) {
        if (cupon.getId() == null || !cuponRepository.existsById(cupon.getId())) {
            throw new RuntimeException("No existe un cupón con ese ID");
        }
        return cuponRepository.save(cupon);
    }

    @Override
    /**
     * Elimina un cupón por ID. Lanza excepción si no existe.
     */
    public void eliminarCupon(String id) {
        if (!cuponRepository.existsById(id)) {
            throw new RuntimeException("No existe un cupón con ese ID");
        }
        cuponRepository.deleteById(id);
    }

    @Override
    /**
     * Retorna todos los cupones registrados.
     */
    public List<Cupon> listarCupones() {
        return cuponRepository.findAll();
    }

    @Override
    /**
     * Busca cupones por cédula del cliente.
     */
    public List<Cupon> buscarCuponesPorCliente(String cedulaCliente) {
        return cuponRepository.findByCedulaCliente(cedulaCliente);
    }

    @Override
    /**
     * Busca cupones por estado.
     */
    public List<Cupon> buscarCuponesPorEstado(EstadoCupon estado) {
        return cuponRepository.findByEstado(estado);
    }

    @Override
    /**
     * Busca cupones por tipo.
     */
    public List<Cupon> buscarCuponesPorTipo(TipoCupon tipoCupon) {
        return cuponRepository.findByTipoCupon(tipoCupon);
    }

    @Override
    /**
     * Busca cupones disponibles por cédula del cliente.
     */
    public List<Cupon> buscarCuponesDisponiblesPorCliente(String cedulaCliente) {
        return cuponRepository.findByCedulaClienteAndEstado(cedulaCliente, EstadoCupon.DISPONIBLE);
    }

    @Override
    /**
     * Valida si un cupón está disponible para uso.
     * Verifica que exista, esté disponible y no haya vencido.
     */
    public boolean validarCupon(String codigo) {
        Optional<Cupon> cuponOpt = cuponRepository.findByCodigo(codigo);
        
        if (cuponOpt.isEmpty()) {
            return false;
        }
        
        Cupon cupon = cuponOpt.get();
        
        // Verificar estado
        if (cupon.getEstado() != EstadoCupon.DISPONIBLE) {
            return false;
        }
        
        // Verificar fecha de vencimiento
        if (cupon.getFechaVencimiento() != null && cupon.getFechaVencimiento().before(new Date())) {
            return false;
        }
        
        return true;
    }
}
