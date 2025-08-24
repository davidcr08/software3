package uniquindio.product.services.interfaces;

import uniquindio.product.model.Pago;
import java.util.List;
import java.util.Optional;

public interface PagoService {
    Pago crearPago(Pago pago);
    Optional<Pago> obtenerPago(String id);
    List<Pago> obtenerPagosPorTipo(String tipoPago);
    List<Pago> obtenerPagosPorEstado(String estado);
    Optional<Pago> obtenerPagoPorCodigoAutorizacion(String codigoAutorizacion);
    Pago actualizarPago(Pago pago);
    void eliminarPago(String id);
    List<Pago> listarPagos();
}