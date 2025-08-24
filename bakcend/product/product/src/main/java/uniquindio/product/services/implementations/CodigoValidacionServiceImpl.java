package uniquindio.product.services.implementations;

import uniquindio.product.model.CodigoValidacion;
import uniquindio.product.repositories.CodigoValidacionRepository;
import uniquindio.product.services.interfaces.CodigoValidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodigoValidacionServiceImpl implements CodigoValidacionService {

    private final CodigoValidacionRepository codigoValidacionRepository;

    @Override
    public CodigoValidacion crearCodigo(CodigoValidacion codigoValidacion) {
        if (codigoValidacionRepository.existsByCodigo(codigoValidacion.getCodigo())) {
            throw new RuntimeException("Ya existe un código con ese valor");
        }
        return codigoValidacionRepository.save(codigoValidacion);
    }

    @Override
    public Optional<CodigoValidacion> obtenerCodigo(Long id) {
        if (!codigoValidacionRepository.existsById(id)) {
            throw new RuntimeException("No existe un código con ese ID");
        }
        return codigoValidacionRepository.findById(id);
    }

    @Override
    public Optional<CodigoValidacion> obtenerCodigoPorCodigo(String codigo) {
        if (!codigoValidacionRepository.existsByCodigo(codigo)) {
            throw new RuntimeException("No existe un código con ese valor");
        }
        return codigoValidacionRepository.findByCodigo(codigo);
    }

    @Override
    public boolean validarCodigo(String codigo) {
        return codigoValidacionRepository.existsByCodigo(codigo);
    }

    @Override
    public void eliminarCodigo(Long id) {
        if (!codigoValidacionRepository.existsById(id)) {
            throw new RuntimeException("No existe un código con ese ID");
        }
        codigoValidacionRepository.deleteById(id);
    }
}