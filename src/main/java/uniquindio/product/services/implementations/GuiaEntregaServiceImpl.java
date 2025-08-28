package uniquindio.product.services.implementations;

import uniquindio.product.model.documents.GuiaEntrega;
import uniquindio.product.repositories.GuiaEntregaRepository;
import uniquindio.product.services.interfaces.GuiaEntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GuiaEntregaServiceImpl implements GuiaEntregaService {

    private final GuiaEntregaRepository guiaEntregaRepository;

    @Override
    public GuiaEntrega crearGuiaEntrega(GuiaEntrega guiaEntrega) {
        return guiaEntregaRepository.save(guiaEntrega);
    }

    @Override
    public Optional<GuiaEntrega> obtenerGuia(String id) {
        if (!guiaEntregaRepository.existsById(id)) {
            throw new RuntimeException("No existe una guía de entrega con ese ID");
        }
        return guiaEntregaRepository.findById(id);
    }

    @Override
    public List<GuiaEntrega> obtenerGuiasPorOrdenCompra(String idOrdenCompra) {
        return guiaEntregaRepository.findByIdOrdenCompra(idOrdenCompra);
    }

    @Override
    public List<GuiaEntrega> obtenerGuiasPorEstado(String estado) {
        return guiaEntregaRepository.findByEstadoGuia(estado);
    }

    @Override
    public GuiaEntrega actualizarGuia(GuiaEntrega guiaEntrega) {
        if (!guiaEntregaRepository.existsById(guiaEntrega.getId())) {
            throw new RuntimeException("No existe una guía de entrega con ese ID");
        }
        return guiaEntregaRepository.save(guiaEntrega);
    }

    @Override
    public void eliminarGuia(String id) {
        if (!guiaEntregaRepository.existsById(id)) {
            throw new RuntimeException("No existe una guía de entrega con ese ID");
        }
        guiaEntregaRepository.deleteById(id);
    }

    @Override
    public List<GuiaEntrega> listarGuias() {
        return guiaEntregaRepository.findAll();
    }
}