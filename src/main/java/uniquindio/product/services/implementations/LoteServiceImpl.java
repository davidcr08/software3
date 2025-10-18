package uniquindio.product.services.implementations;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.product.dto.lote.*;
import uniquindio.product.exceptions.LoteException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.mapper.LoteMapper;
import uniquindio.product.model.documents.Lote;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.enums.EstadoLote;
import uniquindio.product.repositories.LoteRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.LoteService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class LoteServiceImpl implements LoteService {

    private final LoteRepository loteRepository;
    private final ProductoRepository productoRepository;

    @Override
    public String crearLote(CrearLoteDTO crearLoteDTO) throws LoteException, ProductoException {
        Objects.requireNonNull(crearLoteDTO, "El DTO de creación de lote no puede ser nulo");

        // Validar que el producto existe
        Producto producto = productoRepository.findById(crearLoteDTO.idProducto())
                .orElseThrow(() -> new ProductoException(
                        "No se encontró el producto con ID: " + crearLoteDTO.idProducto()
                ));

        // Validar que el código de lote sea único
        if (loteRepository.existsByCodigoLote(crearLoteDTO.codigoLote())) {
            throw new LoteException(
                    "Ya existe un lote con el código: " + crearLoteDTO.codigoLote()
            );
        }

        // Validación de negocio: fecha de vencimiento después de producción
        if (crearLoteDTO.fechaVencimiento().isBefore(crearLoteDTO.fechaProduccion())) {
            throw new LoteException(
                    "La fecha de vencimiento no puede ser anterior a la fecha de producción"
            );
        }

        // Crear entidad desde DTO
        Lote lote = LoteMapper.toEntity(crearLoteDTO);
        lote.setEstado(EstadoLote.EN_PRODUCCION);
        lote.setCantidadDisponible(0); // Aún no está en almacén
        lote.setFechaCreacion(LocalDateTime.now());

        Lote loteGuardado = loteRepository.save(lote);

        return loteGuardado.getId();
    }

    @Override
    public MostrarLoteDTO obtenerLote(String idLote) throws LoteException {
        if (idLote == null || idLote.isBlank()) {
            throw new LoteException("El ID del lote no puede ser nulo o vacío");
        }

        Lote lote = loteRepository.findById(idLote)
                .orElseThrow(() -> new LoteException(
                        "No se encontró el lote con ID: " + idLote
                ));

        String nombreProducto = obtenerNombreProducto(lote.getIdProducto());

        return LoteMapper.toMostrarLoteDTO(lote, nombreProducto);
    }

    @Override
    public MostrarLoteDTO actualizarLote(String idLote, ActualizarLoteDTO dto) throws LoteException, ProductoException {
        if (idLote == null || idLote.isBlank()) {
            throw new LoteException("El ID del lote no puede ser nulo o vacío");
        }
        Objects.requireNonNull(dto, "El DTO de actualización no puede ser nulo");

        Lote lote = loteRepository.findById(idLote)
                .orElseThrow(() -> new LoteException(
                        "No se encontró el lote con ID: " + idLote
                ));

        // Validar que el producto existe si se está cambiando
        if (dto.idProducto() != null && !dto.idProducto().equals(lote.getIdProducto())) {
            productoRepository.findById(dto.idProducto())
                    .orElseThrow(() -> new ProductoException(
                            "No se encontró el producto con ID: " + dto.idProducto()
                    ));
        }

        // Validar código de lote único si se está cambiando
        if (dto.codigoLote() != null && !dto.codigoLote().equals(lote.getCodigoLote())) {
            if (loteRepository.existsByCodigoLote(dto.codigoLote())) {
                throw new LoteException(
                        "Ya existe un lote con el código: " + dto.codigoLote()
                );
            }
        }

        // Validar lógica de negocio de fechas si se están actualizando
        LocalDate fechaProduccion = dto.fechaProduccion() != null ? dto.fechaProduccion() : lote.getFechaProduccion();
        LocalDate fechaVencimiento = dto.fechaVencimiento() != null ? dto.fechaVencimiento() : lote.getFechaVencimiento();

        if (fechaVencimiento.isBefore(fechaProduccion)) {
            throw new LoteException(
                    "La fecha de vencimiento no puede ser anterior a la fecha de producción"
            );
        }

        // Validar cantidades si se están actualizando
        Integer cantidadProducida = dto.cantidadProducida() != null ? dto.cantidadProducida() : lote.getCantidadProducida();
        Integer cantidadDisponible = dto.cantidadDisponible() != null ? dto.cantidadDisponible() : lote.getCantidadDisponible();

        if (cantidadDisponible > cantidadProducida) {
            throw new LoteException(
                    "La cantidad disponible no puede ser mayor a la cantidad producida"
            );
        }

        // Aplicar cambios (actualización parcial)
        LoteMapper.actualizarEntidad(lote, dto);

        Lote loteActualizado = loteRepository.save(lote);

        String nombreProducto = obtenerNombreProducto(loteActualizado.getIdProducto());
        return LoteMapper.toMostrarLoteDTO(loteActualizado, nombreProducto);
    }

    @Override
    public void eliminarLote(String idLote) throws LoteException {
        if (idLote == null || idLote.isBlank()) {
            throw new LoteException("El ID del lote no puede ser nulo o vacío");
        }

        Lote lote = loteRepository.findById(idLote)
                .orElseThrow(() -> new LoteException(
                        "No se encontró el lote con ID: " + idLote
                ));

        // Validar que no se pueda eliminar un lote que ya tiene ventas
        if (lote.getCantidadDisponible() < lote.getCantidadProducida()) {
            throw new LoteException(
                    "No se puede eliminar el lote " + lote.getCodigoLote() +
                            " porque ya tiene ventas asociadas. Cantidad vendida: " +
                            (lote.getCantidadProducida() - lote.getCantidadDisponible())
            );
        }

        // Validar que el lote no esté en estado DISPONIBLE en inventario
        if (lote.getEstado() == EstadoLote.DISPONIBLE) {
            throw new LoteException(
                    "No se puede eliminar el lote " + lote.getCodigoLote() +
                            " porque está disponible en inventario. Primero debe bloquearlo o agotarlo"
            );
        }
        loteRepository.delete(lote);
    }

    @Override
    public void ajustarCantidadLote(AjustarCantidadLoteDTO dto) throws LoteException {
        Objects.requireNonNull(dto, "El DTO de ajuste no puede ser nulo");

        Lote lote = loteRepository.findById(dto.idLote())
                .orElseThrow(() -> new LoteException("No se encontró el lote con ID: " + dto.idLote()));

        if (lote.getEstado() == EstadoLote.BLOQUEADO) {
            throw new LoteException("El lote " + lote.getCodigoLote() + " está bloqueado y no puede modificarse");
        }

        if (dto.ajuste() == null || dto.ajuste() <= 0) {
            throw new LoteException("La cantidad de ajuste debe ser un número positivo");
        }

        int nuevaCantidad = lote.getCantidadDisponible() - dto.ajuste();

        if (nuevaCantidad < 0) {
            throw new LoteException(
                    "Stock insuficiente en el lote " + lote.getCodigoLote() +
                            ". Disponible: " + lote.getCantidadDisponible() +
                            ", requerido: " + dto.ajuste()
            );
        }

        lote.setCantidadDisponible(nuevaCantidad);

        if (nuevaCantidad == 0) {
            lote.setEstado(EstadoLote.AGOTADO);
        }

        loteRepository.save(lote);
    }


    @Override
    public void bloquearLote(String idLote, String motivo) throws LoteException {
        if (idLote == null || idLote.isBlank()) {
            throw new LoteException("El ID del lote no puede ser nulo o vacío");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new LoteException("Debe especificar el motivo del bloqueo");
        }

        Lote lote = loteRepository.findById(idLote)
                .orElseThrow(() -> new LoteException(
                        "No se encontró el lote con ID: " + idLote
                ));

        // Validar que no esté ya bloqueado
        if (lote.getEstado() == EstadoLote.BLOQUEADO) {
            throw new LoteException(
                    "El lote " + lote.getCodigoLote() + " ya está bloqueado"
            );
        }

        // Validar que no esté agotado
        if (lote.getEstado() == EstadoLote.AGOTADO) {
            throw new LoteException(
                    "No se puede bloquear el lote " + lote.getCodigoLote() +
                            " porque ya está agotado"
            );
        }

        lote.setEstado(EstadoLote.BLOQUEADO);

        // Agregar motivo a observaciones
        String observacionActual = lote.getObservaciones() != null ? lote.getObservaciones() + " | " : "";
        lote.setObservaciones(observacionActual + "BLOQUEADO: " + motivo);

        loteRepository.save(lote);
    }

    @Override
    public void desbloquearLote(String idLote) throws LoteException {
        if (idLote == null || idLote.isBlank()) {
            throw new LoteException("El ID del lote no puede ser nulo o vacío");
        }

        Lote lote = loteRepository.findById(idLote)
                .orElseThrow(() -> new LoteException(
                        "No se encontró el lote con ID: " + idLote
                ));

        // Validar que esté bloqueado
        if (lote.getEstado() != EstadoLote.BLOQUEADO) {
            throw new LoteException(
                    "El lote " + lote.getCodigoLote() + " no está bloqueado. Estado actual: " + lote.getEstado()
            );
        }

        // Validar si está vencido
        if (lote.estaVencido()) {
            throw new LoteException(
                    "No se puede desbloquear el lote " + lote.getCodigoLote() +
                            " porque está vencido (venció el " + lote.getFechaVencimiento() + ")"
            );
        }

        // Determinar estado apropiado al desbloquear
        if (lote.getCantidadDisponible() == 0) {
            lote.setEstado(EstadoLote.AGOTADO);
        } else if (lote.getCantidadDisponible() > 0) {
            lote.setEstado(EstadoLote.DISPONIBLE);
        }

        loteRepository.save(lote);
    }

    @Override
    public List<MostrarLoteDTO> listarLotesPorProducto(String idProducto) throws ProductoException {
        if (idProducto == null || idProducto.isBlank()) {
            throw new ProductoException("El ID del producto no puede ser nulo o vacío");
        }

        // Validar que el producto existe
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoException(
                        "No se encontró el producto con ID: " + idProducto
                ));

        List<Lote> lotes = loteRepository.findByIdProducto(idProducto);

        return lotes.stream()
                .map(lote -> LoteMapper.toMostrarLoteDTO(lote, producto.getNombreProducto()))
                .toList();
    }

    @Override
    public List<MostrarLoteDTO> listarLotesDisponibles() {
        List<Lote> lotes = loteRepository.findByEstadoAndCantidadDisponibleGreaterThan(
                EstadoLote.DISPONIBLE,
                0
        );
        return getMostrarLoteDTOS(lotes);
    }

    @Override
    public List<LotePorVencerDTO> obtenerLotesPorVencer(int diasUmbral) {
        if (diasUmbral < 0) {
            throw new IllegalArgumentException("El umbral de días debe ser mayor o igual a 0");
        }

        LocalDate fechaLimite = LocalDate.now().plusDays(diasUmbral);

        List<Lote> lotes = loteRepository.findByEstadoAndFechaVencimientoBeforeAndCantidadDisponibleGreaterThan(
                EstadoLote.DISPONIBLE,
                fechaLimite,
                0
        );

        List<String> idsProductos = lotes.stream()
                .map(Lote::getIdProducto)
                .distinct()
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);

        return lotes.stream()
                .map(lote -> {
                    String nombreProducto = productos.stream()
                            .filter(p -> p.getIdProducto().equals(lote.getIdProducto()))
                            .findFirst()
                            .map(Producto::getNombreProducto)
                            .orElse("Producto no disponible");

                    return new LotePorVencerDTO(
                            lote.getCodigoLote(),
                            nombreProducto,
                            lote.getFechaVencimiento(),
                            lote.getCantidadDisponible(),
                            lote.diasParaVencer()
                    );
                })
                .sorted((l1, l2) -> Long.compare(l1.diasRestantes(), l2.diasRestantes()))
                .toList();
    }

    @Override
    public List<MostrarLoteDTO> listarLotesPorEstado(EstadoLote estado) {
        Objects.requireNonNull(estado, "El estado no puede ser nulo");
        List<Lote> lotes = loteRepository.findByEstado(estado);
        return getMostrarLoteDTOS(lotes);
    }

    @Override
    public Lote seleccionarLoteFEFO(String idProducto, Integer cantidadRequerida) throws LoteException, ProductoException {
        if (idProducto == null || idProducto.isBlank()) {
            throw new ProductoException("El ID del producto no puede ser nulo o vacío");
        }
        if (cantidadRequerida == null || cantidadRequerida <= 0) {
            throw new LoteException("La cantidad requerida debe ser mayor a 0");
        }

        // Validar que el producto existe
        productoRepository.findById(idProducto)
                .orElseThrow(() -> new ProductoException(
                        "No se encontró el producto con ID: " + idProducto
                ));

        // Buscar lotes disponibles con stock, no vencidos, ordenados por FEFO
        LocalDate hoy = LocalDate.now();
        List<Lote> lotesDisponibles = loteRepository
                .findByIdProductoAndEstadoAndCantidadDisponibleGreaterThanAndFechaVencimientoAfterOrderByFechaVencimientoAsc(
                        idProducto,
                        EstadoLote.DISPONIBLE,
                        0,
                        hoy
                );

        if (lotesDisponibles.isEmpty()) {
            throw new LoteException(
                    "No hay lotes disponibles para el producto con ID: " + idProducto
            );
        }

        // Seleccionar el primer lote con stock suficiente

        return lotesDisponibles.stream()
                .filter(lote -> lote.getCantidadDisponible() >= cantidadRequerida)
                .findFirst()
                .orElseThrow(() -> new LoteException(
                        "No hay ningún lote con stock suficiente. Requerido: " + cantidadRequerida +
                                ", Máximo disponible: " + lotesDisponibles.stream()
                                .mapToInt(Lote::getCantidadDisponible)
                                .max()
                                .orElse(0)
                ));
    }

    // ============================
    // Métodos privados auxiliares
    // ============================

    @NotNull
    private List<MostrarLoteDTO> getMostrarLoteDTOS(List<Lote> lotes) {
        List<String> idsProductos = lotes.stream()
                .map(Lote::getIdProducto)
                .distinct()
                .toList();

        List<Producto> productos = productoRepository.findAllById(idsProductos);

        return lotes.stream()
                .map(lote -> {
                    String nombreProducto = productos.stream()
                            .filter(p -> p.getIdProducto().equals(lote.getIdProducto()))
                            .findFirst()
                            .map(Producto::getNombreProducto)
                            .orElse("Producto no disponible");

                    return LoteMapper.toMostrarLoteDTO(lote, nombreProducto);
                })
                .toList();
    }

    private String obtenerNombreProducto(String idProducto) {
        return productoRepository.findById(idProducto)
                .map(Producto::getNombreProducto)
                .orElse("Producto no disponible");
    }
}
