package uniquindio.product.controllers;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para manejar las operaciones de MercadoPago
 * Permite crear preferencias de pago y gestionar transacciones
 */
@RestController
@RequestMapping("/api/mercado-pago")
public class MercadoPagoController {

    /**
     * Endpoint para crear una preferencia de pago en MercadoPago
     * 
     * @param productId   String - ID único del producto
     * @param title       String - Título del producto
     * @param description String - Descripción del producto
     * @param pictureUrl  String - URL de la imagen del producto
     * @param categoryId  String - Categoría del producto
     * @param quantity    int - Cantidad de unidades
     * @param currencyId  String - Moneda (ej: BRL, USD, EUR)
     * @param unitPrice   BigDecimal - Precio unitario del producto
     * @return String - URL de inicialización para el sandbox de MercadoPago
     * @throws MPException    Excepción general de MercadoPago
     * @throws MPApiException Excepción específica de la API de MercadoPago
     */
    @GetMapping("/mercadopago")
    public String mercadoPago(
            @RequestParam String productId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String pictureUrl,
            @RequestParam String categoryId,
            @RequestParam int quantity,
            @RequestParam String currencyId,
            @RequestParam BigDecimal unitPrice) throws MPException, MPApiException {

        // Configurar el token de acceso de MercadoPago (token de prueba)
        MercadoPagoConfig.setAccessToken("TEST-6953429914324853-083019-53a1f01950753c65187808a170437881-119672768");

        // Configurar las URLs de retorno para diferentes estados del pago
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("https://www.tu-sitio/success") // URL para pagos exitosos
                .pending("https://www.tu-sitio/pending") // URL para pagos pendientes
                .failure("https://www.tu-sitio/failure") // URL para pagos fallidos
                .build();

        // Crear el item/producto para la preferencia de pago usando parámetros
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(productId) // ID único del producto
                .title(title) // Título del producto
                .description(description) // Descripción del producto
                .pictureUrl(pictureUrl) // URL de la imagen del producto
                .categoryId(categoryId) // Categoría del producto
                .quantity(quantity) // Cantidad de unidades
                .currencyId(currencyId) // Moneda
                .unitPrice(unitPrice) // Precio unitario
                .build();

        // Crear lista de items y agregar el producto
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        // Construir la solicitud de preferencia con items y URLs de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .backUrls(backUrls)
                .build();

        // Crear cliente de preferencias de MercadoPago
        PreferenceClient client = new PreferenceClient();

        // Crear la preferencia en MercadoPago
        Preference preference = client.create(preferenceRequest);

        // Retornar la URL de inicialización para el sandbox
        return preference.getSandboxInitPoint();

    }
}
