package uniquindio.product.services.interfaces;

import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.exceptions.ProductoException;

public interface InventarioService {

    void inicializarInventario() throws InventarioException;

    boolean verificarDisponibilidad(String idProducto, Integer cantidad) throws ProductoException;

    Integer obtenerStockDisponible(String idProducto) throws ProductoException;

    void reducirStock(String idProducto, Integer cantidad) throws ProductoException, InventarioException;

    void modificarStock(String idProducto, Integer cantidad) throws ProductoException, InventarioException;
}