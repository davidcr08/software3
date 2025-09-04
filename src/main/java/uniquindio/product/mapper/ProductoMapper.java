package uniquindio.product.mapper;

import uniquindio.product.dto.producto.CrearProductoDTO;
import uniquindio.product.dto.producto.ItemProductoDTO;
import uniquindio.product.dto.producto.ProductoDetalleDTO;
import uniquindio.product.model.documents.Producto;

public class ProductoMapper {

    private ProductoMapper() {} // evitar instanciación

    public static Producto toEntity(CrearProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombreProducto(dto.nombre());
        producto.setImagenProducto(dto.imagenProducto());
        producto.setCantidad(dto.cantidad());
        producto.setValor(dto.valor());
        producto.setTipo(dto.tipo());
        return producto;
    }

    public static ProductoDetalleDTO toDetalleDTO(Producto producto) {
        return new ProductoDetalleDTO(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getImagenProducto(),
                producto.getCantidad(),
                producto.getUltimaFechaModificacion(),
                producto.getValor(),
                producto.getTipo()
        );
    }

    public static ItemProductoDTO toItemDTO(Producto producto) {
        return new ItemProductoDTO(
                producto.getIdProducto(),
                producto.getNombreProducto(),
                producto.getImagenProducto(),
                producto.getCantidad(),
                producto.getValor(),
                producto.getTipo()
        );
    }
}
