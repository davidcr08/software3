package uniquindio.product.services.implementations;

import uniquindio.product.model.OrdenCompra;
import uniquindio.product.repositories.OrdenCompraRepository;
import uniquindio.product.services.interfaces.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;

    @Override
    public OrdenCompra crearOrdenCompra(OrdenCompra ordenCompra) {
        return ordenCompraRepository.save(ordenCompra);
    }

    @Override
    public Optional<OrdenCompra> obtenerOrdenCompra(String id) {
        if (!ordenCompraRepository.existsById(id)) {
            throw new RuntimeException("No existe una orden de compra con ese ID");
        }
        return ordenCompraRepository.findById(id);
    }

    @Override
    public List<OrdenCompra> obtenerOrdenesPorCliente(String idCliente) {
        return ordenCompraRepository.findByIdCliente(idCliente);
    }

    @Override
    public List<OrdenCompra> obtenerOrdenesPorSeller(String idSeller) {
        return ordenCompraRepository.findByIdSeller(idSeller);
    }

    @Override
    public List<OrdenCompra> obtenerOrdenesPorEstado(String estado) {
        return ordenCompraRepository.findByEstadoOrdenCompra(estado);
    }

    @Override
    public OrdenCompra actualizarOrdenCompra(OrdenCompra ordenCompra) {
        if (!ordenCompraRepository.existsById(ordenCompra.getId())) {
            throw new RuntimeException("No existe una orden de compra con ese ID");
        }
        return ordenCompraRepository.save(ordenCompra);
    }

    @Override
    public void eliminarOrdenCompra(String id) {
        if (!ordenCompraRepository.existsById(id)) {
            throw new RuntimeException("No existe una orden de compra con ese ID");
        }
        ordenCompraRepository.deleteById(id);
    }

    @Override
    public List<OrdenCompra> listarOrdenesCompra() {
        return ordenCompraRepository.findAll();
    }
}