package uniquindio.product.services.interfaces;

import uniquindio.product.dto.inventario.DetalleLoteDTO;
import uniquindio.product.dto.inventario.ProductoBajoStockDTO;
import uniquindio.product.dto.inventario.ResumenInventarioDTO;
import uniquindio.product.dto.inventario.StockPorLoteDTO;
import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.exceptions.LoteException;
import uniquindio.product.exceptions.ProductoException;

import java.util.List;

public interface InventarioService {

    void inicializarInventario() throws InventarioException;
    Integer obtenerStockDisponible(String idProducto) throws ProductoException;
    List<DetalleLoteDTO> listarLotes();
    List<ResumenInventarioDTO> obtenerResumenInventario();
    //Stock detallado por lote
    List<StockPorLoteDTO> obtenerStockPorLote(String idProducto) throws ProductoException;
    //Alertas de stock bajo (suma de todos los lotes)
    List<ProductoBajoStockDTO> obtenerProductosBajoStock(int umbral);
    void registrarEntradaAlmacen(String idLote) throws LoteException, InventarioException;

}