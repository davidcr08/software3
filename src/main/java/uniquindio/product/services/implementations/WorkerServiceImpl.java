package uniquindio.product.services.implementations;

import uniquindio.product.enums.TipoWorker;
import uniquindio.product.model.Worker;

import uniquindio.product.repositories.WorkerRepository;
import uniquindio.product.services.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerServiceImpl implements WorkerService {

    private final WorkerRepository workerRepository;

    @Override
    public Worker crearWorker(Worker worker) {
        if (workerRepository.existsBySellerId(worker.getSellerId())) {
            throw new RuntimeException("Ya existe un worker con ese seller ID");
        }

        if (workerRepository.existsByNumeroContrato(worker.getNumeroContrato())) {
            throw new RuntimeException("Ya existe un worker con ese número de contrato");
        }

        if (workerRepository.existsByEmail(worker.getEmail())) {
            throw new RuntimeException("Ya existe un worker con ese email");
        }

        worker.setActivo(true);
        return workerRepository.save(worker);
    }

    @Override
    public Optional<Worker> obtenerWorker(String id) {
        if (!workerRepository.existsById(id)) {
            throw new RuntimeException("No existe un worker con ese ID");
        }
        return workerRepository.findById(id);
    }

    @Override
    public Optional<Worker> obtenerWorkerPorSellerId(String sellerId) {
        if (!workerRepository.existsBySellerId(sellerId)) {
            throw new RuntimeException("No existe un worker con ese seller ID");
        }
        return workerRepository.findBySellerId(sellerId);
    }

    @Override
    public Optional<Worker> obtenerWorkerPorNumeroContrato(String numeroContrato) {
        if (!workerRepository.existsByNumeroContrato(numeroContrato)) {
            throw new RuntimeException("No existe un worker con ese número de contrato");
        }
        return workerRepository.findByNumeroContrato(numeroContrato);
    }

    @Override
    public Optional<Worker> obtenerWorkerPorEmail(String email) {
        if (!workerRepository.existsByEmail(email)) {
            throw new RuntimeException("No existe un worker con ese email");
        }
        return workerRepository.findByEmail(email);
    }

    @Override
    public List<Worker> obtenerWorkersPorTipo(TipoWorker tipoWorker) {
        return workerRepository.findByTipoWorker(tipoWorker);
    }

    @Override
    public Worker actualizarWorker(Worker worker) {
        if (!workerRepository.existsById(worker.getId())) {
            throw new RuntimeException("No existe un worker con ese ID");
        }
        return workerRepository.save(worker);
    }

    @Override
    public void eliminarWorker(String id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe un worker con ese ID"));
        worker.setActivo(false);
        workerRepository.save(worker);
    }

    @Override
    public List<Worker> listarWorkers() {
        return workerRepository.findAll();
    }
}