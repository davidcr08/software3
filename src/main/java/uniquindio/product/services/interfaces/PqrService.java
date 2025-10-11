package uniquindio.product.services.interfaces;

import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;

import java.util.List;

public interface PqrService {
    PqrResponseDTO crearPqr(CrearPqrDTO crearPqrDTO);
    PqrResponseDTO consultarPqrIdPqr(String idPqr);
    PqrResponseDTO consultarPqrIdWorker(String idWorker);
    PqrResponseDTO consultarPqrIdUsuario(String idUsuario);
    List<PqrResponseDTO> consultarTodasPqr();
}
