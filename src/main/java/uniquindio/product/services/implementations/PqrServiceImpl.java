package uniquindio.product.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;
import uniquindio.product.mapper.PqrMapper;
import uniquindio.product.model.documents.PQR;
import uniquindio.product.repositories.PqrRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.PqrService;

import java.util.ArrayList;
import java.util.List;

import static uniquindio.product.mapper.PqrMapper.toEntity;

@Service
public class PqrServiceImpl implements PqrService {

    @Autowired
    private PqrRepository pqrRepository;

    @Override
    public PqrResponseDTO crearPqr(CrearPqrDTO crearPqrDTO) {
        PQR pqr = toEntity(crearPqrDTO);
        pqrRepository.save(pqr);
        return PqrMapper.toResponseDTO(pqr);
    }

    @Override
    public PqrResponseDTO consultarPqrIdPqr(String idPqr) {
        try {
            return PqrMapper.toResponseDTO(pqrRepository.findByIdPqr(idPqr));
        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public PqrResponseDTO consultarPqrIdWorker(String idWorker) {
        try {
            return PqrMapper.toResponseDTO(pqrRepository.findByIdWorker(idWorker));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PqrResponseDTO consultarPqrIdUsuario(String idUsuario) {
        try {
            return PqrMapper.toResponseDTO(pqrRepository.findByIdUsuario(idUsuario));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<PqrResponseDTO> consultarTodasPqr() {
        List<PQR> pqrs = pqrRepository.findAll();
        List<PqrResponseDTO> pqrDTOs = new ArrayList<>();
        for (PQR pqr : pqrs) {
            pqrDTOs.add(PqrMapper.toResponseDTO(pqr));
        }
        return pqrDTOs;
    }

}
