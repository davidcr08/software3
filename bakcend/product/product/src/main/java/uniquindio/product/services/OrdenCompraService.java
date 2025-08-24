package uniquindio.product.services;

import uniquindio.product.model.OrdenCompra;
import uniquindio.product.repositories.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    public OrdenCompra guardarOrdenCompra(OrdenCompra ordenCompra) {
        return ordenCompraRepository.save(ordenCompra);
    }

    public Optional<OrdenCompra> obtenerOrdenCompraPorId(String id) {
        return ordenCompraRepository.findById(id);
    }

    public List<OrdenCompra> obtenerOrdenesPorCliente(String idCliente) {
        return ordenCompraRepository.findByIdCliente(idCliente);
    }

    public List<OrdenCompra> obtenerOrdenesPorSeller(String idSeller) {
        return ordenCompraRepository.findByIdSeller(idSeller);
    }

    public List<OrdenCompra> obtenerOrdenesPorEstado(String estado) {
        return ordenCompraRepository.findByEstadoOrdenCompra(estado);
    }

    public List<OrdenCompra> obtenerOrdenesConCostoMayorA(Double costoMinimo) {
        return ordenCompraRepository.findByCostoTotalGreaterThan(costoMinimo);
    }

    public List<OrdenCompra> obtenerTodasOrdenes() {
        return ordenCompraRepository.findAll();
    }

    public void eliminarOrdenCompra(String id) {
        ordenCompraRepository.deleteById(id);
    }
}