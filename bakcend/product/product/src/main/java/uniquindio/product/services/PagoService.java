package uniquindio.product.services;

import uniquindio.product.model.Pago;
import uniquindio.product.repositories.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public Pago guardarPago(Pago pago) {
        return pagoRepository.save(pago);
    }

    public Optional<Pago> obtenerPagoPorId(String id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> obtenerPagosPorTipo(String tipoPago) {
        return pagoRepository.findByTipoPago(tipoPago);
    }

    public List<Pago> obtenerPagosPorEstado(String estado) {
        return pagoRepository.findByEstado(estado);
    }

    public Optional<Pago> obtenerPagoPorCodigoAutorizacion(String codigoAutorizacion) {
        return pagoRepository.findByCodigoAutorizacion(codigoAutorizacion);
    }

    public List<Pago> obtenerPagosConValorMayorA(Double valorMinimo) {
        return pagoRepository.findByValorTransaccionGreaterThan(valorMinimo);
    }

    public List<Pago> obtenerTodosPagos() {
        return pagoRepository.findAll();
    }

    public void eliminarPago(String id) {
        pagoRepository.deleteById(id);
    }
}