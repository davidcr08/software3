package uniquindio.product.services;

import uniquindio.product.enums.TipoWorker;
import uniquindio.product.model.Worker;

import uniquindio.product.repositories.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    public Worker guardarWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public Optional<Worker> obtenerWorkerPorId(String id) {
        return workerRepository.findById(id);
    }

    public Optional<Worker> obtenerWorkerPorSellerId(String sellerId) {
        return workerRepository.findBySellerId(sellerId);
    }

    public Optional<Worker> obtenerWorkerPorNumeroContrato(String numeroContrato) {
        return workerRepository.findByNumeroContrato(numeroContrato);
    }

    public Optional<Worker> obtenerWorkerPorEmail(String email) {
        return workerRepository.findByEmail(email);
    }

    public List<Worker> obtenerWorkersPorTipo(TipoWorker tipoWorker) {
        return workerRepository.findByTipoWorker(tipoWorker);
    }

    public List<Worker> obtenerWorkersPorEstado(Boolean activo) {
        return workerRepository.findByActivo(activo);
    }

    public List<Worker> obtenerTodosWorkers() {
        return workerRepository.findAll();
    }

    public void eliminarWorker(String id) {
        workerRepository.deleteById(id);
    }

    public boolean existeWorkerPorSellerId(String sellerId) {
        return workerRepository.existsBySellerId(sellerId);
    }

    public boolean existeWorkerPorNumeroContrato(String numeroContrato) {
        return workerRepository.existsByNumeroContrato(numeroContrato);
    }

    public boolean existeWorkerPorEmail(String email) {
        return workerRepository.existsByEmail(email);
    }
}