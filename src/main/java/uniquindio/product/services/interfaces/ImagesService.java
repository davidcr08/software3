package uniquindio.product.services.interfaces;

import org.springframework.web.multipart.MultipartFile;
import uniquindio.product.dto.producto.ImagenDTO;

import java.util.Map;

public interface ImagesService {

    ImagenDTO subirImagen(MultipartFile imagen) throws Exception;
    String eliminarImagen(String idImagen) throws Exception;

}
