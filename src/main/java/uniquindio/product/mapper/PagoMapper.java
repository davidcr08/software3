package uniquindio.product.mapper;

import com.mercadopago.resources.payment.Payment;
import uniquindio.product.model.enums.EstadoPago;
import uniquindio.product.model.enums.Moneda;
import uniquindio.product.model.enums.TipoPago;
import uniquindio.product.model.vo.Pago;

import java.math.BigDecimal;

public class PagoMapper {

    private PagoMapper() {
        // Evita instanciación
    }

    public static Pago toPago(Payment payment) {
        Pago pago = new Pago();
        pago.setIdPago(payment.getId().toString());
        pago.setFecha(payment.getDateCreated());
        pago.setEstado(mapEstado(payment.getStatus()));
        pago.setDetalleEstado(payment.getStatusDetail());
        pago.setTipoPago(mapTipoPago(payment.getPaymentTypeId()));
        pago.setMoneda(mapMoneda(payment.getCurrencyId()));
        pago.setCodigoAutorizacion(payment.getAuthorizationCode());
        pago.setValorTransaccion(BigDecimal.valueOf(payment.getTransactionAmount().doubleValue()));
        pago.setMetodoPago(payment.getPaymentMethodId());
        return pago;
    }

    private static EstadoPago mapEstado(String status) {
        if (status == null) return EstadoPago.PENDIENTE;
        return switch (status.toLowerCase()) {
            case "approved" -> EstadoPago.APROBADO;
            case "rejected" -> EstadoPago.RECHAZADO;
            case "in_process", "pending" -> EstadoPago.PENDIENTE;
            default -> EstadoPago.FALLIDO;
        };
    }

    private static TipoPago mapTipoPago(String paymentTypeId) {
        if (paymentTypeId == null) return null;
        return switch (paymentTypeId.toLowerCase()) {
            case "credit_card" -> TipoPago.TARJETA_CREDITO;
            case "debit_card" -> TipoPago.TARJETA_DEBITO;
            case "account_money", "bank_transfer" -> TipoPago.TRANSFERENCIA;
            default -> null;
        };
    }

    private static Moneda mapMoneda(String currencyId) {
        if (currencyId == null) return Moneda.COP; // por defecto
        return switch (currencyId.toUpperCase()) {
            case "USD" -> Moneda.USD;
            case "EUR" -> Moneda.EUR;
            default -> Moneda.COP;
        };
    }
}