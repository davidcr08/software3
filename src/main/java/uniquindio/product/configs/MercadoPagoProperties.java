package uniquindio.product.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mercadopago")
@Getter
@Setter
public class MercadoPagoProperties {
    private String token;
    private String notificationUrl;
    private String successUrl;
    private String failureUrl;
    private String pendingUrl;
}
