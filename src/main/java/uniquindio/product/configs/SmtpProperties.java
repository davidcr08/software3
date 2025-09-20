package uniquindio.product.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "smtp")
@Getter
@Setter
public class SmtpProperties {
    private String host;
    private int port;
    private String username;
    private String password;
}
