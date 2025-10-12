package uniquindio.product.services.implementations;

import uniquindio.product.dto.carrito.*;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.mapper.CarritoMapper;
import uniquindio.product.model.documents.Carrito;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.documents.Usuario;
import uniquindio.product.model.vo.DetalleCarrito;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.repositories.CarritoRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.repositories.UsuarioRepository;
import uniquindio.product.services.interfaces.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uniquindio.product.services.interfaces.InventarioService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioService inventarioService;

    @Override
    public void crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException {

        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(carritoDTO.idUsuario())
                .orElseThrow(() -> new CarritoException("El usuario con ID " + carritoDTO.idUsuario() + " no existe."));

        // Verificar si ya tiene carrito
        if (carritoRepository.existsByUsuario(usuario)) {
            throw new CarritoException("El usuario ya tiene un carrito creado");
        }

        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario); // ← Asignar el objeto Usuario completo
        carrito.setItems(new ArrayList<>()); // iniciar vacío

        carritoRepository.save(carrito);
    }

    private Carrito obtenerCarritoEntity(String idUsuario) throws CarritoException {
        return carritoRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new CarritoException("No se encontró un carrito para el usuario con ID: " + idUsuario));
    }

    /**
     * Agrega uno o varios ítems al carrito de un usuario.
     * Si el producto ya existe en el carrito, se incrementa su cantidad.
     *
     * @param idUsuario identificador del usuario dueño del carrito
     * @param nuevosItemsDTO lista de ítems a agregar
     * @return DTO con la información actualizada del carrito
     * @throws CarritoException si el carrito no existe o los ítems son inválidos
     */
    @Override
    @Transactional
    public CarritoDTO agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO) throws CarritoException {
        if (nuevosItemsDTO == null || nuevosItemsDTO.isEmpty()) {
            throw new CarritoException("La lista de ítems no puede estar vacía.");
        }

        // Validar stock antes de agregar al carrito
        for (DetalleCarritoDTO item : nuevosItemsDTO) {
            if (!inventarioService.verificarDisponibilidad(item.idProducto(), item.cantidad())) {
                Integer stockDisponible = inventarioService.obtenerStockDisponible(item.idProducto());
                throw new CarritoException("Stock insuficiente para el producto: " + item.idProducto() +
                        ". Stock disponible: " + stockDisponible +
                        ", cantidad solicitada: " + item.cantidad());
            }
        }

        Carrito carrito = obtenerCarritoEntity(idUsuario);

        nuevosItemsDTO.forEach(dto -> {
            carrito.agregarOActualizarItem(new DetalleCarrito(dto.idProducto(), dto.cantidad()));
        });

        Carrito actualizado = carritoRepository.saveAndFlush(carrito);

        return CarritoMapper.toDTO(actualizado);
    }

    /**
     * Elimina un ítem específico del carrito de un usuario.
     *
     * @param idUsuario identificador del usuario dueño del carrito
     * @param idProducto identificador del producto a eliminar
     * @return DTO con el carrito actualizado
     * @throws CarritoException si el carrito no existe o el producto no está en el carrito
     */
    @Override
    @Transactional
    public CarritoDTO eliminarItemDelCarrito(String idUsuario, String idProducto) throws CarritoException {
        Carrito carrito = obtenerCarritoEntity(idUsuario);

        boolean eliminado = carrito.eliminarItem(idProducto);
        if (!eliminado) {
            throw new CarritoException("No se encontró el producto en el carrito del usuario.");
        }

        Carrito actualizado = carritoRepository.save(carrito);

        return CarritoMapper.toDTO(actualizado);
    }

    /**
     * Vacía todos los ítems del carrito de un usuario.
     *
     * @param idUsuario identificador del usuario dueño del carrito
     * @return DTO con el carrito vacío
     * @throws CarritoException si el carrito no existe
     */
    @Override
    @Transactional
    public CarritoDTO vaciarCarrito(String idUsuario) throws CarritoException {
        Carrito carrito = obtenerCarritoEntity(idUsuario);

        carrito.vaciar();

        Carrito actualizado = carritoRepository.save(carrito);

        return CarritoMapper.toDTO(actualizado);
    }

    /**
     * Lista los productos en el carrito de un usuario con detalle de cantidades y subtotales.
     *
     * @param idUsuario identificador del usuario
     * @return lista de productos en el carrito con información detallada
     * @throws CarritoException si el carrito no existe
     * @throws ProductoException si algún producto del carrito no existe en la BD
     */
    @Override
    @Transactional(readOnly = true)
    public List<InformacionProductoCarritoDTO> listarProductosEnCarrito(String idUsuario)
            throws CarritoException, ProductoException {

        Carrito carrito = obtenerCarritoEntity(idUsuario);

        return carrito.getItems().stream()
                .map(item -> {
                    Producto producto = productoRepository.findById(item.getIdProducto())
                            .orElseThrow(() -> new ProductoException(
                                    "El producto con ID " + item.getIdProducto() + " no existe."));

                    Double subtotal = producto.getValor() * item.getCantidad();

                    return new InformacionProductoCarritoDTO(
                            new DetalleCarritoDTO(item.getIdProducto(), item.getCantidad()),
                            producto.getImagenProducto(),
                            producto.getNombreProducto(),
                            producto.getValor(),
                            subtotal
                    );
                })
                .toList();
    }

    /**
     * Calcula el valor total del carrito de un usuario.
     *
     * @param idUsuario identificador del usuario dueño del carrito
     * @return total en valor monetario del carrito
     * @throws CarritoException  si el carrito no existe
     * @throws ProductoException si algún producto no se encuentra
     */
    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalCarrito(String idUsuario) throws CarritoException, ProductoException {
        Carrito carrito = obtenerCarritoEntity(idUsuario);

        return carrito.getItems().stream()
                .mapToDouble(item -> {
                    Producto producto = productoRepository.findById(item.getIdProducto())
                            .orElseThrow(() -> new ProductoException(
                                    "Producto no encontrado para el ID: " + item.getIdProducto()
                            ));
                    return producto.getValor() * item.getCantidad();
                })
                .sum();
    }

    /**
     * Obtiene el carrito completo de un usuario, incluyendo:
     * - Información de los productos en el carrito
     * - Subtotales por producto
     * - Total acumulado del carrito
     * - Cantidad total de productos
     *
     * @param idUsuario identificador del usuario
     * @return DTO con la información detallada del carrito
     * @throws CarritoException  si el carrito no existe
     * @throws ProductoException si algún producto no se encuentra
     */
    @Override
    @Transactional(readOnly = true)
    public CarritoResponseDTO obtenerCarritoCompleto(String idUsuario) throws CarritoException, ProductoException {
        Carrito carrito = obtenerCarritoEntity(idUsuario);

        List<InformacionProductoCarritoDTO> items = new ArrayList<>();
        double total = 0.0;

        for (DetalleCarrito item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ProductoException(
                            "El producto con ID " + item.getIdProducto() + " no existe."
                    ));

            double subtotal = producto.getValor() * item.getCantidad();
            total += subtotal;

            items.add(new InformacionProductoCarritoDTO(
                    new DetalleCarritoDTO(item.getIdProducto(), item.getCantidad()),
                    producto.getImagenProducto(),
                    producto.getNombreProducto(),
                    producto.getValor(),
                    subtotal
            ));
        }

        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getUsuario().getId(),
                items,
                total,
                carrito.cantidadTotal()
        );
    }
}