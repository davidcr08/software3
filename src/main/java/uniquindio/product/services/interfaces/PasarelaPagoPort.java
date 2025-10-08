package uniquindio.product.services.interfaces;

import com.mercadopago.resources.preference.Preference;
import uniquindio.product.exceptions.PedidoException;
import uniquindio.product.model.documents.Pedido;
import uniquindio.product.model.vo.Pago;

public interface PasarelaPagoPort {
    Preference crearPreferencia(Pedido pedido) throws PedidoException;
    Pago obtenerPago(String idPago) throws PedidoException;
}
