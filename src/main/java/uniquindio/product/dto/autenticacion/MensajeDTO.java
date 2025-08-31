package uniquindio.product.dto.autenticacion;

public record MensajeDTO<T>(
        boolean error,
        T respuesta
) {
}
