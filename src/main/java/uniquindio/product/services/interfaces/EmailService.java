package uniquindio.product.services.interfaces;

import uniquindio.product.dto.email.EmailDTO;
import uniquindio.product.exceptions.EmailException;

public interface EmailService {

    void enviarCorreo(EmailDTO emailDTO) throws EmailException;

}
