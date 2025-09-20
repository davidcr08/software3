package uniquindio.product.services.implementations;

import uniquindio.product.services.interfaces.CodigoValidacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class CodigoValidacionServiceImpl implements CodigoValidacionService {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom secureRandom = new SecureRandom();


    /**
     * Genera un código de validación con longitud personalizada.
     * @param longitud número de caracteres del código.
     * @return código de validación.
     */
    public String generarCodigoValidacion(int longitud) {
        StringBuilder codigo = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int index = secureRandom.nextInt(CARACTERES.length());
            codigo.append(CARACTERES.charAt(index));
        }
        return codigo.toString();
    }
}