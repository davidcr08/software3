package uniquindio.product.services.interfaces;

import uniquindio.product.dto.lote.*;
import uniquindio.product.exceptions.LoteException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.enums.EstadoLote;

import java.util.List;

public interface LoteService {

    String crearLote(CrearLoteDTO dto) throws LoteException, ProductoException;
    MostrarLoteDTO obtenerLote(String idLote) throws LoteException;
    MostrarLoteDTO actualizarLote(String idLote, ActualizarLoteDTO dto) throws LoteException, ProductoException;
    void eliminarLote(String idLote) throws LoteException;
    void ajustarCantidadLote(AjustarCantidadLoteDTO dto) throws LoteException;
    void bloquearLote(String idLote, String motivo) throws LoteException;
    void desbloquearLote(String idLote) throws LoteException;
    List<MostrarLoteDTO> listarLotesPorProducto(String idProducto) throws ProductoException;
    List<MostrarLoteDTO> listarLotesDisponibles();
    List<LotePorVencerDTO> obtenerLotesPorVencer(int diasUmbral);
    List<MostrarLoteDTO> listarLotesPorEstado(EstadoLote estado);
    Lote seleccionarLoteFEFO(String idProducto, Integer cantidadRequerida) throws LoteException, ProductoException;
}