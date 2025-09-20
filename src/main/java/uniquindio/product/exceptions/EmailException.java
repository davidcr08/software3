package uniquindio.product.exceptions;

public class EmailException extends Exception{

    //Metodo que permite enviar mensaje de error mediante una excepcion
    public EmailException(String message) {
        super(message);
    }
}
