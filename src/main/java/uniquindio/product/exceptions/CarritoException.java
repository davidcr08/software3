package uniquindio.product.exceptions;

public class CarritoException extends Exception{

    //Metodo que permite enviar mensaje de error mediante una excepcion
    public CarritoException(String message) {
        super(message);
    }
}