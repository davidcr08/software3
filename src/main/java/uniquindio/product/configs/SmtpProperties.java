package uniquindio.product.configs;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void check() {
        String passDisplay = password != null ? "******" : "NULL";
        System.out.println("SMTP config -> user=" + username + ", pass=" + passDisplay);
    }
}
