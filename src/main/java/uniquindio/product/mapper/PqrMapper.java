package uniquindio.product.mapper;

import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;
import uniquindio.product.model.documents.PQR;

public class PqrMapper {
    private PqrMapper() {} // evitar instanciación

    public static PQR toEntity(PqrResponseDTO responseDTO) {
        PQR pqr = new PQR();
        pqr.setIdPqr(responseDTO.idPqr());
        pqr.setIdUsuario(responseDTO.idUsuario());
        pqr.setCategoria(responseDTO.categoriaPqr());
        pqr.setDescripcion(responseDTO.descripcion());
        pqr.setIdWorker(responseDTO.idWorker());
        pqr.setFechaCreacion(responseDTO.fechaCreacion());
        pqr.setFechaRespuesta(responseDTO.fechaRespuesta());
        pqr.setEstadoPqr(responseDTO.estadoPqr());
        return pqr;
    }

    public static PQR toEntity(CrearPqrDTO crearPqrDTO) {
        PQR pqr = new PQR();
        pqr.setIdUsuario(crearPqrDTO.idUsuario());
        pqr.setCategoria(crearPqrDTO.categoriaPqr());
        pqr.setDescripcion(crearPqrDTO.descripcion());
        pqr.setIdWorker(crearPqrDTO.idWorker());
        pqr.setFechaCreacion(crearPqrDTO.fechaCreacion());
        pqr.setFechaRespuesta(crearPqrDTO.fechaRespuesta());
        pqr.setEstadoPqr(crearPqrDTO.estadoPqr());
        return pqr;
    }

    public static PqrResponseDTO toResponseDTO(PQR pqr) {
        return new PqrResponseDTO(
                pqr.getIdPqr(),
                pqr.getIdUsuario(),
                pqr.getCategoria(),
                pqr.getDescripcion(),
                pqr.getIdWorker(),
                pqr.getFechaCreacion(),
                pqr.getFechaRespuesta(),
                pqr.getEstadoPqr()
        );
    }

}
