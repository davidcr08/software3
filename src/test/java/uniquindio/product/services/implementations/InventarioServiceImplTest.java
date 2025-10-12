package uniquindio.product.services.implementations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uniquindio.product.exceptions.InventarioException;
import uniquindio.product.exceptions.ProductoException;
import uniquindio.product.model.documents.Inventario;
import uniquindio.product.model.documents.Producto;
import uniquindio.product.model.enums.TipoProducto;
import uniquindio.product.model.vo.DetalleInventario;
import uniquindio.product.repositories.InventarioRepository;
import uniquindio.product.repositories.ProductoRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Producto producto;
    private Inventario inventario;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba que se usan en múltiples tests
        producto = new Producto();
        producto.setIdProducto("prod-123");
        producto.setNombreProducto("Producto Test");
        producto.setDescripcion("Descripción del producto de prueba");
        producto.setValor(100.0);
        producto.setTipo(TipoProducto.CUIDADO_PIEL);
        inventario = new Inventario();
        inventario.setIdInventario("inventario-principal");
        inventario.setUltimaActualizacion(LocalDateTime.now());

        // Agregar stock inicial al producto
        DetalleInventario detalle = new DetalleInventario("prod-123", 10, LocalDateTime.now());
        inventario.getDetalleInventario().add(detalle);
    }

    @Test
    void cuandoInicializarInventarioYNoExiste_entoncesSeCrea() throws InventarioException {
        // Given - Simular que no existe inventario
        when(inventarioRepository.count()).thenReturn(0L);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // When
        inventarioService.inicializarInventario();

        // Then - Verificar que se guardó el inventario
        verify(inventarioRepository, times(1)).save(any(Inventario.class));
    }

    @Test
    void cuandoVerificarDisponibilidadYHayStock_entoncesRetornaTrue() throws ProductoException {
        // Given
        when(productoRepository.existsById("prod-123")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));

        // When
        boolean disponible = inventarioService.verificarDisponibilidad("prod-123", 5);

        // Then
        assertThat(disponible).isTrue();
    }

    @Test
    void cuandoVerificarDisponibilidadYNoHayStock_entoncesRetornaFalse() throws ProductoException {
        // Given
        when(productoRepository.existsById("prod-123")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));

        // When - Intentar comprar más de lo disponible
        boolean disponible = inventarioService.verificarDisponibilidad("prod-123", 15);

        // Then
        assertThat(disponible).isFalse();
    }

    @Test
    void cuandoVerificarDisponibilidadYProductoNoExiste_entoncesLanzaExcepcion() {
        // Given
        when(productoRepository.existsById("prod-inexistente")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> inventarioService.verificarDisponibilidad("prod-inexistente", 5))
                .isInstanceOf(ProductoException.class)
                .hasMessageContaining("El producto con ID prod-inexistente no existe");
    }

    @Test
    void cuandoReducirStockConStockSuficiente_entoncesSeReduceCorrectamente()
            throws ProductoException, InventarioException {
        // Given
        when(productoRepository.existsById("prod-123")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // When
        inventarioService.reducirStock("prod-123", 3);

        // Then - Verificar que se guardó el inventario actualizado
        verify(inventarioRepository, times(1)).save(inventario);

        // El stock debería ser 10 - 3 = 7
        Integer stockActual = inventarioService.obtenerStockDisponible("prod-123");
        assertThat(stockActual).isEqualTo(7);
    }

    @Test
    void cuandoReducirStockSinStockSuficiente_entoncesLanzaExcepcion() {
        // Given
        when(productoRepository.existsById("prod-123")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));

        // When & Then - Intentar reducir más stock del disponible
        assertThatThrownBy(() -> inventarioService.reducirStock("prod-123", 20))
                .isInstanceOf(InventarioException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void cuandoModificarStockConProductoExistente_entoncesActualizaStock()
            throws ProductoException, InventarioException {
        // Given
        when(productoRepository.existsById("prod-123")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // When - Establecer stock en 25 unidades
        inventarioService.modificarStock("prod-123", 25);

        // Then
        verify(inventarioRepository, times(1)).save(inventario);

        // Verificar que el stock se estableció en 25 (no se suma)
        Integer stockActual = inventarioService.obtenerStockDisponible("prod-123");
        assertThat(stockActual).isEqualTo(25);
    }

    @Test
    void cuandoModificarStockConProductoNuevo_entoncesCreaStock()
            throws ProductoException, InventarioException {
        // Given - Producto que no existe en inventario
        when(productoRepository.existsById("prod-nuevo")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventario);

        // When
        inventarioService.modificarStock("prod-nuevo", 30);

        // Then
        verify(inventarioRepository, times(1)).save(inventario);

        Integer stockActual = inventarioService.obtenerStockDisponible("prod-nuevo");
        assertThat(stockActual).isEqualTo(30);
    }

    @Test
    void cuandoObtenerStockDeProductoNuevo_entoncesRetornaCero() throws ProductoException {
        // Given - Producto que no está en el inventario
        when(productoRepository.existsById("prod-nuevo")).thenReturn(true);
        when(inventarioRepository.findById("inventario-principal")).thenReturn(Optional.of(inventario));

        // When
        Integer stock = inventarioService.obtenerStockDisponible("prod-nuevo");

        // Then
        assertThat(stock).isZero();
    }
}