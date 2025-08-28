package uniquindio.product.controllers;

import uniquindio.product.enums.TipoWorker;
import uniquindio.product.model.Worker;
import uniquindio.product.services.interfaces.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping
    public ResponseEntity<Worker> crearWorker(@RequestBody Worker worker) {
        try {
            Worker workerCreado = workerService.crearWorker(worker);
            return ResponseEntity.ok(workerCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> obtenerWorker(@PathVariable String id) {
        try {
            return workerService.obtenerWorker(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Worker> obtenerWorkerPorSellerId(@PathVariable String sellerId) {
        try {
            return workerService.obtenerWorkerPorSellerId(sellerId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/contrato/{numeroContrato}")
    public ResponseEntity<Worker> obtenerWorkerPorNumeroContrato(@PathVariable String numeroContrato) {
        try {
            return workerService.obtenerWorkerPorNumeroContrato(numeroContrato)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Worker> obtenerWorkerPorEmail(@PathVariable String email) {
        try {
            return workerService.obtenerWorkerPorEmail(email)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/tipo/{tipoWorker}")
    public ResponseEntity<List<Worker>> obtenerWorkersPorTipo(@PathVariable TipoWorker tipoWorker) {
        try {
            List<Worker> workers = workerService.obtenerWorkersPorTipo(tipoWorker);
            return ResponseEntity.ok(workers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping
    public ResponseEntity<Worker> actualizarWorker(@RequestBody Worker worker) {
        try {
            Worker workerActualizado = workerService.actualizarWorker(worker);
            return ResponseEntity.ok(workerActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarWorker(@PathVariable String id) {
        try {
            workerService.eliminarWorker(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Worker>> listarWorkers() {
        try {
            List<Worker> workers = workerService.listarWorkers();
            return ResponseEntity.ok(workers);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}