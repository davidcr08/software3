package uniquindio.product.controllers;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.resources.preference.Preference;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPApiException;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.documents.Usuario;
import uniquindio.product.services.interfaces.PedidoService;


/**
 * Controlador para manejar las operaciones de MercadoPago
 * Permite crear preferencias de pago y gestionar transacciones
 */
@RestController
@RequestMapping("/api/mercado-pago")
public class MercadoPagoController {

        Pedido Clientepedido = new Pedido();
        int IdPedido = 0;


        //Obtener la ID del carrito
        @GetMapping("/pedido/{idPedido}")
        public int obtenerIdPedido(@PathVariable int idPedido) {
                return idPedido;
        }




        //Copiar el carrito a un objeto para no tener que buscar mas en la BD
        public void AsignarPedido(int idPedido) throws Throwable {
                // Buscar en la base de datos
                SimpleJpaRepository pedidoRepository = null;
                Pedido pedidoBD = (Pedido) pedidoRepository.findById(idPedido)
                        .orElseThrow(() -> new RuntimeException("Pedido no encontrado con id: " + idPedido));

                // Crear un nuevo objeto en memoria y copiar los datos
                Clientepedido = new Pedido();
                Clientepedido.setId(pedidoBD.getId());
                Clientepedido.setIdCliente(pedidoBD.getIdCliente());
                Clientepedido.setCodigoPasarela(pedidoBD.getCodigoPasarela());
                // 👆 aquí copias todos los atributos que tenga tu entidad Pedido

                System.out.println("Pedido cargado en memoria con id: " + Clientepedido.getId());
        }

        //crear el titulo para el pedido



        @GetMapping("/mercadopago")
        public String mercadoPago() throws MPException, MPApiException {

                // Configurar el token de acceso de MercadoPago (token de prueba)
                MercadoPagoConfig.setAccessToken(
                                "TEST-6953429914324853-083019-53a1f01950753c65187808a170437881-119672768");

                // Configurar las URLs de retorno para diferentes estados del pago
                PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                                .success("https://www.tu-sitio/success") // URL para pagos exitosos
                                .pending("https://www.tu-sitio/pending") // URL para pagos pendientes
                                .failure("https://www.tu-sitio/failure") // URL para pagos fallidos
                                .build();

                // Crear el item/producto para la preferencia de pago usando parámetros
                PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                                .id(Clientepedido.getId()) // ID único del producto
                                .title("Pedido") // Título del producto
                                .description("description") // Descripción del producto
                                .pictureUrl("pictureUrl") // URL de la imagen del producto
                                .categoryId("categoryId") // Categoría del producto
                                .quantity(5) // Cantidad de unidades
                                .currencyId(Clientepedido.getId()) // Moneda
                                .unitPrice(BigDecimal.valueOf(Clientepedido.getTotal())) // Precio unitario
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
