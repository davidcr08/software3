package uniquindio.product.services.implementations;

import uniquindio.product.dto.carrito.*;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Carrito;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.vo.DetalleCarrito;
import uniquindio.product.exceptions.CarritoException;
import uniquindio.product.repositories.CarritoRepository;
import uniquindio.product.repositories.ProductoRepository;
import uniquindio.product.services.interfaces.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServiceImpl implements CarritoService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;

    @Override
    public void crearCarrito(CrearCarritoDTO carritoDTO) throws CarritoException {
        if (carritoRepository.existsByIdUsuario(carritoDTO.idUsuario())) {
            throw new CarritoException("El usuario ya tiene un carrito creado");
        }

        Carrito carrito = new Carrito();
        carrito.setIdUsuario(carritoDTO.idUsuario());
        carrito.setItems(convertirItemsDTOAItems(carritoDTO.itemsCarrito()));

        carritoRepository.save(carrito);
    }

    @Override
    public Carrito obtenerCarritoPorUsuario(String idUsuario) throws CarritoException {
        return carritoRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new CarritoException("No se encontró un carrito para el usuario con ID: " + idUsuario));
    }

    @Override
    public Carrito agregarItemsAlCarrito(String idUsuario, List<DetalleCarritoDTO> nuevosItemsDTO) throws CarritoException {
        if (nuevosItemsDTO == null || nuevosItemsDTO.isEmpty()) {
            throw new CarritoException("La lista de ítems no puede estar vacía.");
        }

        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        List<DetalleCarrito> nuevosItems = convertirItemsDTOAItems(nuevosItemsDTO);

        for (DetalleCarrito nuevoItem : nuevosItems) {
            Optional<DetalleCarrito> itemExistente = carrito.getItems().stream()
                    .filter(item -> item.getIdProducto().equals(nuevoItem.getIdProducto()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                // Sumar cantidades si el producto ya existe
                DetalleCarrito existente = itemExistente.get();
                existente.setCantidad(existente.getCantidad() + nuevoItem.getCantidad());
            } else {
                // Agregar nuevo item
                carrito.getItems().add(nuevoItem);
            }
        }

        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito eliminarItemDelCarrito(String idUsuario, String idProducto) throws CarritoException {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        boolean itemEliminado = carrito.getItems().removeIf(item ->
                item.getIdProducto().equals(idProducto)
        );

        if (!itemEliminado) {
            throw new CarritoException("No se encontró el producto en el carrito del usuario.");
        }

        return carritoRepository.save(carrito);
    }

    @Override
    public Carrito vaciarCarrito(String idUsuario) throws CarritoException {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        carrito.getItems().clear();
        return carritoRepository.save(carrito);
    }

    @Override
    public List<InformacionProductoCarritoDTO> listarProductosEnCarrito(String idUsuario) throws CarritoException, ProductoException {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        List<InformacionProductoCarritoDTO> detallesConProductos = new ArrayList<>();

        for (DetalleCarrito item : carrito.getItems()) {
            Producto producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new ProductoException("El producto con ID " + item.getIdProducto() + " no existe."));

            DetalleCarritoDTO detalle = new DetalleCarritoDTO(
                    item.getIdProducto(),
                    item.getCantidad()
            );

            Double subtotal = producto.getValor() * item.getCantidad();

            InformacionProductoCarritoDTO informacionProducto = new InformacionProductoCarritoDTO(
                    detalle,
                    producto.getImagenProducto(),
                    "Producto " + producto.getTipo().toString(),
                    producto.getValor(),
                    subtotal
            );

            detallesConProductos.add(informacionProducto);
        }

        return detallesConProductos;
    }

    @Override
    public Double calcularTotalCarrito(String idUsuario) throws CarritoException {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);

        return carrito.getItems().stream()
                .mapToDouble(item -> {
                    Producto producto = null;
                    try {
                        producto = productoRepository.findById(item.getIdProducto())
                                .orElseThrow(() -> new ProductoException("Producto no encontrado para el ID: " + item.getIdProducto()));
                    } catch (ProductoException e) {
                        throw new RuntimeException(e);
                    }
                    return producto.getValor() * item.getCantidad();
                })
                .sum();
    }

    @Override
    public CarritoResponseDTO obtenerCarritoCompleto(String idUsuario) throws CarritoException, ProductoException {
        Carrito carrito = obtenerCarritoPorUsuario(idUsuario);
        List<InformacionProductoCarritoDTO> items = listarProductosEnCarrito(idUsuario);
        Double total = calcularTotalCarrito(idUsuario);

        return new CarritoResponseDTO(
                carrito.getId(),
                carrito.getIdUsuario(),
                items,
                total
        );
    }

    private List<DetalleCarrito> convertirItemsDTOAItems(List<DetalleCarritoDTO> itemsCarritoDTO) {
        return itemsCarritoDTO.stream()
                .map(dto -> new DetalleCarrito(
                        dto.idProducto(),
                        dto.cantidad()
                ))
                .toList();
    }
}