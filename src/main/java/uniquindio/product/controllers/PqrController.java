package uniquindio.product.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uniquindio.product.dto.autenticacion.MensajeDTO;
import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;
import uniquindio.product.exceptions.PqrException;
import uniquindio.product.services.implementations.PqrServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/pqr")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PqrController {
    private final PqrServiceImpl pqrService;

    @PostMapping
    public ResponseEntity<MensajeDTO<PqrResponseDTO>> crerPqr( @RequestBody CrearPqrDTO prmPqr) throws PqrException{
        return ResponseEntity.ok(new MensajeDTO<PqrResponseDTO>(false, pqrService.crearPqr(prmPqr)));
    }

    @GetMapping("/idPqr/{id}")
    public ResponseEntity<MensajeDTO<PqrResponseDTO>> getPqrId(@PathVariable String id) throws PqrException{
        return ResponseEntity.ok(new MensajeDTO<PqrResponseDTO>(false, pqrService.consultarPqrIdPqr(id)));
    }

    @GetMapping("/idUsuario/{id}")
    public ResponseEntity<MensajeDTO<PqrResponseDTO>> getPqrIdUsuario(@PathVariable String id) throws PqrException{
        return ResponseEntity.ok(new MensajeDTO<PqrResponseDTO>(false, pqrService.consultarPqrIdUsuario(id)));
    }

    @GetMapping("/idWorker/{id}")
    public ResponseEntity<MensajeDTO<PqrResponseDTO>> getPqrIdWorker(@PathVariable String id) throws PqrException{
        return ResponseEntity.ok(new MensajeDTO<PqrResponseDTO>(false, pqrService.consultarPqrIdWorker(id)));
    }

    @GetMapping
    public ResponseEntity<MensajeDTO<List<PqrResponseDTO>>> getAllPqr() throws PqrException {
        List<PqrResponseDTO> pqrList = pqrService.consultarTodasPqr();
        MensajeDTO<List<PqrResponseDTO>> mensaje = new MensajeDTO<>(false, pqrList);
        return ResponseEntity.ok(mensaje);
    }



}
