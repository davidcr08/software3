package uniquindio.product.dto.email;

public record EmailDTO(
        String asunto,
        String cuerpo,
        String destinatario
) {}