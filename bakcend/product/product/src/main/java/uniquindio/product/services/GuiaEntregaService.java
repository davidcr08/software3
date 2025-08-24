package uniquindio.product.services;

import uniquindio.product.model.GuiaEntrega;
import uniquindio.product.repositories.GuiaEntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GuiaEntregaService {

    @Autowired
    private GuiaEntregaRepository guiaEntregaRepository;

    public GuiaEntrega guardarGuiaEntrega(GuiaEntrega guiaEntrega) {
        return guiaEntregaRepository.save(guiaEntrega);
    }

    public Optional<GuiaEntrega> obtenerGuiaPorId(String id) {
        return guiaEntregaRepository.findById(id);
    }

    public List<GuiaEntrega> obtenerGuiasPorOrdenCompra(String idOrdenCompra) {
        return guiaEntregaRepository.findByIdOrdenCompra(idOrdenCompra);
    }

    public List<GuiaEntrega> obtenerGuiasPorEstado(String estado) {
        return guiaEntregaRepository.findByEstadoGuia(estado);
    }

    public List<GuiaEntrega> obtenerTodasGuias() {
        return guiaEntregaRepository.findAll();
    }

    public void eliminarGuia(String id) {
        guiaEntregaRepository.deleteById(id);
    }
}