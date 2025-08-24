package uniquindio.product.services.interfaces;

import uniquindio.product.enums.TipoWorker;
import uniquindio.product.model.Worker;

import java.util.List;
import java.util.Optional;

public interface WorkerService {
    Worker crearWorker(Worker worker);
    Optional<Worker> obtenerWorker(String id);
    Optional<Worker> obtenerWorkerPorSellerId(String sellerId);
    Optional<Worker> obtenerWorkerPorNumeroContrato(String numeroContrato);
    Optional<Worker> obtenerWorkerPorEmail(String email);
    List<Worker> obtenerWorkersPorTipo(TipoWorker tipoWorker);
    Worker actualizarWorker(Worker worker);
    void eliminarWorker(String id);
    List<Worker> listarWorkers();
}