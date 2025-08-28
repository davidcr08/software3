package uniquindio.product.services.interfaces;

import uniquindio.product.model.documents.GuiaEntrega;
import java.util.List;
import java.util.Optional;

public interface GuiaEntregaService {
    GuiaEntrega crearGuiaEntrega(GuiaEntrega guiaEntrega);
    Optional<GuiaEntrega> obtenerGuia(String id);
    List<GuiaEntrega> obtenerGuiasPorOrdenCompra(String idOrdenCompra);
    List<GuiaEntrega> obtenerGuiasPorEstado(String estado);
    GuiaEntrega actualizarGuia(GuiaEntrega guiaEntrega);
    void eliminarGuia(String id);
    List<GuiaEntrega> listarGuias();
}