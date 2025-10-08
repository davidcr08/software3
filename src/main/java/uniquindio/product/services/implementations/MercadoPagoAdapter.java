package uniquindio.product.services.implementations;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import uniquindio.product.configs.MercadoPagoProperties;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.mapper.PagoMapper;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.vo.DetallePedido;
import uniquindio.product.model.vo.Pago;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.PasarelaPagoPort;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoAdapter implements PasarelaPagoPort {

    private final MercadoPagoProperties properties;
    private final ProductoRepository productoRepository;

    @Override
    public Preference crearPreferencia(Pedido pedido) throws PedidoException {
        try {

            if (properties.getToken() == null || properties.getToken().isBlank()) {
                throw new PedidoException("Token de acceso de MercadoPago no configurado.");
            }

            MercadoPagoConfig.setAccessToken(properties.getToken());

            List<PreferenceItemRequest> items = pedido.getDetalle().stream()
                    .map(this::mapItem)
                    .toList();

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(properties.getSuccessUrl())
                    .failure(properties.getFailureUrl())
                    .pending(properties.getPendingUrl())
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .backUrls(backUrls)
                    .autoReturn("approved")
                    .items(items)
                    .metadata(Map.of("id_pedido", pedido.getId()))
                    .notificationUrl(properties.getNotificationUrl())
                    .build();

            PreferenceClient client = new PreferenceClient();
            return client.create(preferenceRequest);

        } catch (Exception e) {
            log.error("Error creando preferencia de pago: {}", e.getMessage(), e);
            throw new PedidoException("Error creando preferencia de pago");
        }
    }

    @Override
    public Pago obtenerPago(String idPago) throws PedidoException {
        try {
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(idPago));
            return PagoMapper.toPago(payment);
        } catch (Exception e) {
            log.error("Error obteniendo pago {}", idPago, e);
            throw new PedidoException("Error al obtener pago desde pasarela");
        }
    }

    private PreferenceItemRequest mapItem(DetallePedido item) {
        Producto producto = productoRepository.findById(item.getIdProducto())
                .orElseThrow(() -> new ProductoException("Producto no encontrado: " + item.getIdProducto()));

        return PreferenceItemRequest.builder()
                .id(producto.getIdProducto())
                .title(producto.getNombreProducto())
                .pictureUrl(producto.getImagenProducto())
                .categoryId(producto.getTipo().name())
                .quantity(item.getCantidad())
                .currencyId("COP")
                .unitPrice(item.getPrecioUnitario())
                .build();
    }
}
