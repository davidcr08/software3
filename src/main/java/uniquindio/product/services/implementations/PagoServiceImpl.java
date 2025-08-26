package uniquindio.product.services.implementations;

import uniquindio.product.model.Pago;
import uniquindio.product.repositories.PagoRepository;
import uniquindio.product.services.interfaces.PagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    @Override
    public Pago crearPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    @Override
    public Optional<Pago> obtenerPago(String id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("No existe un pago con ese ID");
        }
        return pagoRepository.findById(id);
    }

    @Override
    public List<Pago> obtenerPagosPorTipo(String tipoPago) {
        return pagoRepository.findByTipoPago(tipoPago);
    }

    @Override
    public List<Pago> obtenerPagosPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    @Override
    public Optional<Pago> obtenerPagoPorCodigoAutorizacion(String codigoAutorizacion) {
        return pagoRepository.findByCodigoAutorizacion(codigoAutorizacion);
    }

    @Override
    public Pago actualizarPago(Pago pago) {
        if (!pagoRepository.existsById(pago.getId())) {
            throw new RuntimeException("No existe un pago con ese ID");
        }
        return pagoRepository.save(pago);
    }

    @Override
    public void eliminarPago(String id) {
        if (!pagoRepository.existsById(id)) {
            throw new RuntimeException("No existe un pago con ese ID");
        }
        pagoRepository.deleteById(id);
    }

    @Override
    public List<Pago> listarPagos() {
        return pagoRepository.findAll();
    }
}