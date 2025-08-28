package uniquindio.product.services.interfaces;

import uniquindio.product.model.documents.OrdenCompra;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {
    OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra);
    Optional<OrdenCompra> obtenerOrdenCompra(String id);
    List<OrdenCompra> obtenerOrdenesPorCliente(String idCliente);
    List<OrdenCompra> obtenerOrdenesPorSeller(String idSeller);
    List<OrdenCompra> obtenerOrdenesPorEstado(String estado);
    OrdenCompra actualizarOrdenCompra(OrdenCompra ordenCompra);
    void eliminarOrdenCompra(String id);
    List<OrdenCompra> listarOrdenesCompra();
}