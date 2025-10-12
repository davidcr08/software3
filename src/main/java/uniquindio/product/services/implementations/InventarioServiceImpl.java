package uniquindio.product.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Inventario;
import uniquindio.product.repositories.InventarioRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.InventarioService;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    private static final String INVENTARIO_ID = "inventario-principal";

    @Override
    public void inicializarInventario() throws InventarioException {
        if (inventarioRepository.count() == 0) {
            Inventario inventario = new Inventario();
            inventario.setIdInventario(INVENTARIO_ID);
            inventario.setUltimaActualizacion(LocalDateTime.now());
            inventarioRepository.save(inventario);
        }
    }

    private Inventario obtenerInventario() throws InventarioException {
        return inventarioRepository.findById(INVENTARIO_ID)
                .orElseThrow(() -> new InventarioException("Inventario no encontrado. Debe inicializarse primero."));
    }

    private void verificarProductoExiste(String idProducto) throws ProductoException {
        if (!productoRepository.existsById(idProducto)) {
            throw new ProductoException("El producto con ID " + idProducto + " no existe.");
        }
    }

    @Override
    public boolean verificarDisponibilidad(String idProducto, Integer cantidad) throws ProductoException {
        verificarProductoExiste(idProducto);

        try {
            Inventario inventario = obtenerInventario();
            return inventario.tieneStockSuficiente(idProducto, cantidad);
        } catch (InventarioException e) {
            log.warn("Inventario no inicializado, considerando stock 0");
            return false;
        }
    }

    @Override
    public Integer obtenerStockDisponible(String idProducto) throws ProductoException {
        verificarProductoExiste(idProducto);

        try {
            Inventario inventario = obtenerInventario();
            return inventario.obtenerStockDisponible(idProducto);
        } catch (InventarioException e) {
            log.warn("Inventario no inicializado, retornando stock 0");
            return 0;
        }
    }

    @Override
    public void reducirStock(String idProducto, Integer cantidad) throws ProductoException, InventarioException {
        verificarProductoExiste(idProducto);

        if (cantidad <= 0) {
            throw new InventarioException("La cantidad a reducir debe ser mayor a 0");
        }

        Inventario inventario = obtenerInventario();

        if (!inventario.tieneStockSuficiente(idProducto, cantidad)) {
            throw new InventarioException("Stock insuficiente para el producto: " + idProducto +
                    ". Stock disponible: " + inventario.obtenerStockDisponible(idProducto) +
                    ", cantidad requerida: " + cantidad);
        }

        inventario.reducirStock(idProducto, cantidad);
        inventarioRepository.save(inventario);

    }

    @Override
    public void modificarStock(String idProducto, Integer cantidad) throws ProductoException, InventarioException {
        verificarProductoExiste(idProducto);

        if (cantidad < 0) {
            throw new InventarioException("La cantidad no puede ser negativa");
        }

        Inventario inventario = obtenerInventario();

        inventario.actualizarStock(idProducto, cantidad);
        inventarioRepository.save(inventario);


    }
}