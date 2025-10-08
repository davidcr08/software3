package uniquindio.product.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uniquindio.product.dto.pedido.NotificacionPagoDTO;
import uniquindio.product.services.interfaces.PedidoService;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Slf4j
public class WebHookController {

    private final PedidoService pedidoService;

    /**
     * Webhook para recibir notificaciones de MercadoPago.
     * Debe estar accesible públicamente (sin autenticación).
     */
    @PostMapping("/notificacion")
    public ResponseEntity<Void> recibirNotificacion(@RequestBody NotificacionPagoDTO notificacion) {
        try {
            pedidoService.recibirNotificacionMercadoPago(notificacion);
            log.info("Notificación de MercadoPago procesada: {}", notificacion.type());
            return ResponseEntity.ok().build(); // MercadoPago espera solo 200 OK
        } catch (Exception e) {
            log.error("Error procesando notificación de MercadoPago: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}