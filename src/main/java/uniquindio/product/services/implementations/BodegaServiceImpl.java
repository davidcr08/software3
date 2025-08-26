package uniquindio.product.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import uniquindio.product.model.Bodega;
import uniquindio.product.repositories.BodegaRepository;
import uniquindio.product.services.interfaces.BodegaService;

/**
 * Implementación de {@link BodegaService} usando un {@link BodegaRepository} JPA.
 */
@Service
@RequiredArgsConstructor
public class BodegaServiceImpl implements BodegaService {

    private final BodegaRepository bodegaRepository;

    @Override
    /**
     * Persiste una nueva bodega en base de datos.
     */
    public Bodega crearBodega(Bodega bodega) {
        return bodegaRepository.save(bodega);
    }

    @Override
    /**
     * Busca una bodega por ID. Lanza excepción si no existe.
     */
    public Optional<Bodega> obtenerBodega(String id) {
        if (!bodegaRepository.existsById(id)) {
            throw new RuntimeException("No existe una bodega con ese ID");
        }
        return bodegaRepository.findById(id);
    }

    @Override
    /**
     * Actualiza una bodega existente, validando que el ID exista.
     */
    public Bodega actualizarBodega(Bodega bodega) {
        if (bodega.getBodegaId() == null || !bodegaRepository.existsById(bodega.getBodegaId())) {
            throw new RuntimeException("No existe una bodega con ese ID");
        }
        return bodegaRepository.save(bodega);
    }

    @Override
    /**
     * Elimina una bodega por ID. Lanza excepción si no existe.
     */
    public void eliminarBodega(String id) {
        if (!bodegaRepository.existsById(id)) {
            throw new RuntimeException("No existe una bodega con ese ID");
        }
        bodegaRepository.deleteById(id);
    }

    @Override
    /**
     * Retorna todas las bodegas registradas.
     */
    public List<Bodega> listarBodegas() {
        return bodegaRepository.findAll();
    }
}


