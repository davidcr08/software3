package uniquindio.product.services;

import uniquindio.product.model.CodigoValidacion;
import uniquindio.product.repositories.CodigoValidacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CodigoValidacionService {

    @Autowired
    private CodigoValidacionRepository codigoValidacionRepository;

    public CodigoValidacion guardarCodigo(CodigoValidacion codigoValidacion) {
        return codigoValidacionRepository.save(codigoValidacion);
    }

    public Optional<CodigoValidacion> obtenerCodigoPorId(Long id) {
        return codigoValidacionRepository.findById(id);
    }

    public Optional<CodigoValidacion> obtenerCodigoPorCodigo(String codigo) {
        return codigoValidacionRepository.findByCodigo(codigo);
    }

    public boolean validarCodigo(String codigo) {
        return codigoValidacionRepository.existsByCodigo(codigo);
    }

    public void eliminarCodigo(Long id) {
        codigoValidacionRepository.deleteById(id);
    }
}