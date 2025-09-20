package uniquindio.product.services.implementations;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import uniquindio.product.configs.CloudinaryProperties;
import uniquindio.product.dto.producto.ImagenDTO;
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

    /**
     * Sube una imagen a Cloudinary y devuelve un DTO con la información relevante.
     *
     * @param imagen archivo MultipartFile
     * @return ImagenDTO con url, publicId y formato
     * @throws Exception si ocurre algún error en la conversión o subida
     */
    @Override
    public ImagenDTO subirImagen(MultipartFile imagen) throws Exception {
        if (imagen == null || imagen.isEmpty()) {
            throw new IllegalArgumentException("El archivo de imagen no puede estar vacío");
        }

        File file = convertir(imagen);
        try {
            Map<String, Object> uploadResult =
                    cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "ReneChardon"));

            return new ImagenDTO(
                    (String) uploadResult.get("secure_url"),
                    (String) uploadResult.get("public_id"),
                    (String) uploadResult.get("format")
            );
        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * Elimina una imagen de Cloudinary según su publicId.
     *
     * @param idImagen identificador público de la imagen
     * @return String con el resultado de la operación
     * @throws Exception si ocurre un error con la API de Cloudinary
     */
    @Override
    public String eliminarImagen(String idImagen) throws Exception {
        Map<String, Object> result = cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
        return "ok".equals(result.get("result"))
                ? "Imagen eliminada correctamente"
                : "No se pudo eliminar la imagen";
    }

    /**
     * Convierte un MultipartFile a un archivo temporal para enviarlo a Cloudinary.
     *
     * @param imagen archivo recibido
     * @return archivo temporal en disco
     * @throws IOException si ocurre un error en la escritura del archivo
     */
    private File convertir(MultipartFile imagen) throws IOException {
        File file = File.createTempFile("upload-", imagen.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imagen.getBytes());
        }
        return file;
    }
}
