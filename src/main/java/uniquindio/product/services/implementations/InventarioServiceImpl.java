package uniquindio.product.services.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.product.dto.inventario.ProductoBajoStockDTO;
import uniquindio.product.dto.inventario.ResumenInventarioDTO;
import uniquindio.product.dto.inventario.StockPorLoteDTO;
import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.exceptions.LoteException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.mapper.InventarioMapper;
import uniquindio.product.model.documents.Inventario;
import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.enums.EstadoLote;
import uniquindio.product.repositories.InventarioRepository;
import uniquindio.product.repositories.LoteRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.InventarioService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InventarioServiceImpl implements InventarioService {

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;
    private final LoteRepository loteRepository;

    private static final String INVENTARIO_ID = "inventario-principal";

    @Override
    public void inicializarInventario() throws InventarioException {
        // Buscar inventario por ID fijo
        Optional<Inventario> optInventario = inventarioRepository.findById(INVENTARIO_ID);

        if (optInventario.isEmpty()) {
            // No existe, crear uno nuevo
            Inventario inventario = new Inventario();
            inventario.setIdInventario(INVENTARIO_ID);
            inventario.setUltimaActualizacion(LocalDateTime.now());
            inventarioRepository.save(inventario);
            log.info("Inventario principal inicializado");
        } else {
            log.info("Inventario principal ya existe");
        }
    }

    @Override
    public Integer obtenerStockDisponible(String idProducto) throws ProductoException {
        if (idProducto == null || idProducto.isBlank()) {
            throw new ProductoException("El ID del producto no puede ser nulo o vacío");
        }

        // Validar existencia del producto
        productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoException(
                        "No se encontró el producto con ID: " + idProducto
                ));

        LocalDate hoy = LocalDate.now();

        // Consultar lotes disponibles y no vencidos y sumar su stock
        return loteRepository
                .findByIdProductoAndEstadoAndFechaVencimientoAfter(idProducto, EstadoLote.DISPONIBLE, hoy)
                .stream()
                .mapToInt(Lote::getCantidadDisponible)
                .sum();
    }

    @Override
    public List<ResumenInventarioDTO> obtenerResumenInventario() {
        LocalDate hoy = LocalDate.now();

        // Traer solo lotes válidos desde la BD
        List<Lote> lotesValidos = loteRepository
                .findByEstadoAndCantidadDisponibleGreaterThanAndFechaVencimientoAfter(
                        EstadoLote.DISPONIBLE,
                        0,
                        hoy
                );

        if (lotesValidos.isEmpty()) {
            return Collections.emptyList();
        }

        // Agrupar por producto
        Map<String, List<Lote>> lotesPorProducto = lotesValidos.stream()
                .collect(Collectors.groupingBy(Lote::getIdProducto));

        return InventarioMapper.toResumenInventarioDTOList(lotesPorProducto, productoRepository);
    }

    @Override
    public List<StockPorLoteDTO> obtenerStockPorLote(String idProducto) throws ProductoException {
        Objects.requireNonNull(idProducto, "El ID del producto no puede ser nulo");

        if (idProducto.isBlank()) {
            throw new ProductoException("El ID del producto no puede estar vacío");
        }

        // Validar existencia del producto
        productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoException(
                        "No se encontró el producto con ID: " + idProducto
                ));

        LocalDate hoy = LocalDate.now();

        // Obtener lotes asociados, disponibles, con stock y no vencidos
        List<Lote> lotes = loteRepository.findByIdProductoAndEstadoAndCantidadDisponibleGreaterThanAndFechaVencimientoAfterOrderByFechaVencimientoAsc(
                idProducto,
                EstadoLote.DISPONIBLE,
                0,
                hoy
        );

        if (lotes.isEmpty()) {
            return Collections.emptyList();
        }

        // Mapear a DTO
        return InventarioMapper.toStockPorLoteDTOList(lotes);
    }

    @Override
    public List<ProductoBajoStockDTO> obtenerProductosBajoStock(int umbral) {
        if (umbral <= 0) {
            throw new IllegalArgumentException("El umbral debe ser mayor a 0");
        }

        LocalDate hoy = LocalDate.now();

        // Obtener todos los productos activos
        List<Producto> productos = productoRepository.findAll();

        if (productos.isEmpty()) {
            return Collections.emptyList();
        }

        // Obtener TODOS los lotes válidos de una sola vez
        List<Lote> todosLotesValidos = loteRepository
                .findByEstadoAndCantidadDisponibleGreaterThanAndFechaVencimientoAfter(
                        EstadoLote.DISPONIBLE,
                        0,
                        hoy
                );

        // Agrupar lotes por producto en memoria
        Map<String, List<Lote>> lotesPorProducto = todosLotesValidos.stream()
                .collect(Collectors.groupingBy(Lote::getIdProducto));

        // Procesar cada producto
        return productos.stream()
                .map(producto -> {
                    int stockTotal = lotesPorProducto.getOrDefault(producto.getIdProducto(), Collections.emptyList())
                            .stream()
                            .mapToInt(Lote::getCantidadDisponible)
                            .sum();

                    return new AbstractMap.SimpleEntry<>(producto, stockTotal);
                })
                .filter(entry -> entry.getValue() < umbral)
                .map(entry -> InventarioMapper.toProductoBajoStockDTO(
                        entry.getKey(),
                        entry.getValue(),
                        umbral
                ))
                .toList();
    }

    @Override
    public void registrarEntradaAlmacen(String idLote) throws LoteException, InventarioException {
        if (idLote == null || idLote.isBlank()) {
            throw new LoteException("El ID del lote no puede ser nulo o vacío");
        }

        // Buscar el lote
        Lote lote = loteRepository.findById(idLote)
                .orElseThrow(() -> new LoteException("No se encontró el lote con ID: " + idLote));

        if (lote.getEstado() != EstadoLote.EN_PRODUCCION) {
            throw new LoteException("Solo se pueden registrar lotes en estado EN_PRODUCCION");
        }
        if (lote.getCantidadProducida() <= 0) {
            throw new LoteException("El lote no tiene cantidad producida");
        }
        if (lote.estaVencido()) {
            throw new LoteException("No se puede ingresar el lote porque está vencido");
        }

        // Obtener inventario principal (ya existente)
        Inventario inventario = inventarioRepository.findById(INVENTARIO_ID)
                .orElseThrow(() -> new InventarioException("Inventario no inicializado"));

        // Actualizar estado del lote y cantidad disponible
        lote.setEstado(EstadoLote.DISPONIBLE);
        lote.setCantidadDisponible(lote.getCantidadProducida());
        loteRepository.save(lote); // Guardar lote

        // Registrar en inventario
        inventario.agregarLote(lote.getId(), lote.getIdProducto(), lote.getCantidadProducida());

        // **No es necesario llamar a save()** si inventario ya está en la sesión de Hibernate
        log.info("Lote ingresado al almacén: {} - Producto: {} - Cantidad: {}",
                lote.getCodigoLote(), lote.getIdProducto(), lote.getCantidadProducida());
    }

}