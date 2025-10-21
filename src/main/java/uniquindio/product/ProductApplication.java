package uniquindio.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.services.interfaces.InventarioService;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
public class ProductApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

	@Bean
	public CommandLineRunner inicializarInventario(InventarioService inventarioService) {
		return args -> {
			try {
				inventarioService.inicializarInventario();

			} catch (InventarioException e) {
				log.error("Error durante inicialización del inventario", e);
			}
		};
	}
}