package uniquindio.product.services.interfaces;

import uniquindio.product.model.vo.CodigoValidacion;
import java.util.Optional;

public interface CodigoValidacionService {
    CodigoValidacion crearCodigo(CodigoValidacion codigoValidacion);
    Optional<CodigoValidacion> obtenerCodigo(Long id);
    Optional<CodigoValidacion> obtenerCodigoPorCodigo(String codigo);
    boolean validarCodigo(String codigo);
    void eliminarCodigo(Long id);
}