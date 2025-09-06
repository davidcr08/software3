package uniquindio.product.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import uniquindio.product.configs.CloudinaryProperties;
import uniquindio.product.services.interfaces.ImagesService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
@Validated
public class ImagesServiceImp implements ImagesService {

    private final Cloudinary cloudinary;

    public ImagesServiceImp(CloudinaryProperties properties) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", properties.getCloudName(),
                "api_key", properties.getApiKey(),
                "api_secret", properties.getApiSecret()
        ));
    }

    //Se crea un archivo temporal en convertir(), y se borra después de subirlo.
    // Eso para no llenar el disco si se sube muchas imágenes.
    @Override
    public Map subirImagen(MultipartFile imagen) throws Exception {
        File file = convertir(imagen);
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "Panaca"));
        file.delete(); // elimina el archivo temporal
        return uploadResult;    }

    @Override
    public Map eliminarImagen(String idImagen) throws Exception {
        return cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
    }

    private File convertir(MultipartFile imagen) throws IOException {
        File file = File.createTempFile("upload-", imagen.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imagen.getBytes());
        }
        return file;
    }
}
