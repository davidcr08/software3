package uniquindio.product.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImagesService {

    Map subirImagen(MultipartFile imagen) throws Exception;
    Map eliminarImagen(String idImagen) throws Exception;

}
