package uniquindio.product.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;
import uniquindio.product.model.documents.PQR;
import uniquindio.product.model.enums.CategoriaPqr;
import uniquindio.product.model.enums.EstadoPqr;
import uniquindio.product.repositories.PqrRepository;
import uniquindio.product.services.implementations.PqrServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para PqrServiceImpl
 * Prueba todos los métodos del servicio de PQR
 */
@ExtendWith(MockitoExtension.class)
public class PqrTest {

    @Mock
    private PqrRepository pqrRepository;

    @InjectMocks
    private PqrServiceImpl pqrService;

    private CrearPqrDTO crearPqrDTO;
    private PQR pqrEntity;

    @BeforeEach
    void setUp() {
        // Configuración de datos de prueba
        LocalDateTime fechaCreacion = LocalDateTime.now();

        crearPqrDTO = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        pqrEntity = new PQR();
        pqrEntity.setIdPqr("pqr123");
        pqrEntity.setIdUsuario("usuario123");
        pqrEntity.setCategoria(CategoriaPqr.RECLAMO);
        pqrEntity.setDescripcion("Descripción del reclamo");
        pqrEntity.setIdWorker("worker456");
        pqrEntity.setFechaCreacion(fechaCreacion);
        pqrEntity.setFechaRespuesta(null);
        pqrEntity.setEstadoPqr(EstadoPqr.ABIERTO);

    }

    /**
     * Prueba la creación exitosa de un PQR
     * Verifica que el método crearPqr retorna el DTO correcto
     */
    @Test
    void testCrearPqr_Exitoso() {
        // Arrange
        when(pqrRepository.save(any(PQR.class))).thenReturn(pqrEntity);

        // Act
        PqrResponseDTO resultado = pqrService.crearPqr(crearPqrDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario123", resultado.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.categoriaPqr());
        assertEquals("Descripción del reclamo", resultado.descripcion());
        assertEquals("worker456", resultado.idWorker());
        assertEquals(EstadoPqr.ABIERTO, resultado.estadoPqr());

        verify(pqrRepository, times(1)).save(any(PQR.class));
    }

    /**
     * Prueba la consulta de PQR por ID
     * Verifica que retorna el PQR correcto
     */
    @Test
    void testConsultarPqrIdPqr_Exitoso() {
        // Arrange
        String idPqr = "pqr123";
        when(pqrRepository.findByIdPqr(idPqr)).thenReturn(pqrEntity);

        // Act
        PqrResponseDTO resultado = pqrService.consultarPqrIdPqr(idPqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(idPqr, resultado.idPqr());
        assertEquals("usuario123", resultado.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.categoriaPqr());

        verify(pqrRepository, times(1)).findByIdPqr(idPqr);
    }

    /**
     * Prueba la consulta de PQR por ID cuando no existe
     * Verifica que retorna null
     */
    @Test
    void testConsultarPqrIdPqr_NoExiste() {
        // Arrange
        String idPqr = "pqrNoExiste";
        when(pqrRepository.findByIdPqr(idPqr)).thenReturn(null);

        // Act
        PqrResponseDTO resultado = pqrService.consultarPqrIdPqr(idPqr);

        // Assert
        assertNull(resultado);
        verify(pqrRepository, times(1)).findByIdPqr(idPqr);
    }

    /**
     * Prueba la consulta de PQR por ID de worker
     * Verifica que retorna el PQR correcto
     */
    @Test
    void testConsultarPqrIdWorker_Exitoso() {
        // Arrange
        String idWorker = "worker456";
        when(pqrRepository.findByIdWorker(idWorker)).thenReturn(pqrEntity);

        // Act
        PqrResponseDTO resultado = pqrService.consultarPqrIdWorker(idWorker);

        // Assert
        assertNotNull(resultado);
        assertEquals("pqr123", resultado.idPqr());
        assertEquals(idWorker, resultado.idWorker());
        assertEquals("usuario123", resultado.idUsuario());

        verify(pqrRepository, times(1)).findByIdWorker(idWorker);
    }

    /**
     * Prueba la consulta de PQR por ID de worker cuando no existe
     * Verifica que retorna null
     */
    @Test
    void testConsultarPqrIdWorker_NoExiste() {
        // Arrange
        String idWorker = "workerNoExiste";
        when(pqrRepository.findByIdWorker(idWorker)).thenReturn(null);

        // Act
        PqrResponseDTO resultado = pqrService.consultarPqrIdWorker(idWorker);

        // Assert
        assertNull(resultado);
        verify(pqrRepository, times(1)).findByIdWorker(idWorker);
    }

    /**
     * Prueba la consulta de PQR por ID de usuario
     * Verifica que retorna el PQR correcto
     */
    @Test
    void testConsultarPqrIdUsuario_Exitoso() {
        // Arrange
        String idUsuario = "usuario123";
        when(pqrRepository.findByIdUsuario(idUsuario)).thenReturn(pqrEntity);

        // Act
        PqrResponseDTO resultado = pqrService.consultarPqrIdUsuario(idUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("pqr123", resultado.idPqr());
        assertEquals(idUsuario, resultado.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.categoriaPqr());

        verify(pqrRepository, times(1)).findByIdUsuario(idUsuario);
    }

    /**
     * Prueba la consulta de PQR por ID de usuario cuando no existe
     * Verifica que retorna null
     */
    @Test
    void testConsultarPqrIdUsuario_NoExiste() {
        // Arrange
        String idUsuario = "usuarioNoExiste";
        when(pqrRepository.findByIdUsuario(idUsuario)).thenReturn(null);

        // Act
        PqrResponseDTO resultado = pqrService.consultarPqrIdUsuario(idUsuario);

        // Assert
        assertNull(resultado);
        verify(pqrRepository, times(1)).findByIdUsuario(idUsuario);
    }

    /**
     * Prueba la creación de PQR con diferentes categorías
     * Verifica que se manejan correctamente todas las categorías
     */
    @Test
    void testCrearPqr_DiferentesCategorias() {
        // Arrange
        CategoriaPqr[] categorias = {
                CategoriaPqr.SERVICIO_CLIENTE,
                CategoriaPqr.RECLAMO,
                CategoriaPqr.FACTURACION,
                CategoriaPqr.CUPON,
                CategoriaPqr.OTROS
        };

        for (CategoriaPqr categoria : categorias) {
            CrearPqrDTO dto = new CrearPqrDTO(
                    "usuario123",
                    categoria,
                    "Descripción de prueba",
                    "worker456",
                    LocalDateTime.now(),
                    null,
                    EstadoPqr.ABIERTO);

            PQR pqr = new PQR();
            pqr.setIdPqr("pqr" + categoria.name());
            pqr.setCategoria(categoria);
            when(pqrRepository.save(any(PQR.class))).thenReturn(pqr);

            // Act
            PqrResponseDTO resultado = pqrService.crearPqr(dto);

            // Assert
            assertNotNull(resultado);
            assertEquals(categoria, resultado.categoriaPqr());
        }
    }

    /**
     * Prueba la creación de PQR con diferentes estados
     * Verifica que se manejan correctamente todos los estados
     */
    @Test
    void testCrearPqr_DiferentesEstados() {
        // Arrange
        EstadoPqr[] estados = {
                EstadoPqr.ABIERTO,
                EstadoPqr.CERRADO,
                EstadoPqr.RESUELTO,
                EstadoPqr.ABANDONADO
        };

        for (EstadoPqr estado : estados) {
            CrearPqrDTO dto = new CrearPqrDTO(
                    "usuario123",
                    CategoriaPqr.RECLAMO,
                    "Descripción de prueba",
                    "worker456",
                    LocalDateTime.now(),
                    null,
                    estado);

            PQR pqr = new PQR();
            pqr.setIdPqr("pqr" + estado.name());
            pqr.setEstadoPqr(estado);
            when(pqrRepository.save(any(PQR.class))).thenReturn(pqr);

            // Act
            PqrResponseDTO resultado = pqrService.crearPqr(dto);

            // Assert
            assertNotNull(resultado);
            assertEquals(estado, resultado.estadoPqr());
        }
    }

    /**
     * Prueba la creación de PQR con descripción larga
     * Verifica que se maneja correctamente la descripción de 3000 caracteres
     */
    @Test
    void testCrearPqr_DescripcionLarga() {
        // Arrange
        String descripcionLarga = "A".repeat(3000); // Máximo permitido
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionLarga,
                "worker456",
                LocalDateTime.now(),
                null,
                EstadoPqr.ABIERTO);

        PQR pqr = new PQR();
        pqr.setIdPqr("pqr123");
        pqr.setDescripcion(descripcionLarga);
        when(pqrRepository.save(any(PQR.class))).thenReturn(pqr);

        // Act
        PqrResponseDTO resultado = pqrService.crearPqr(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionLarga, resultado.descripcion());
        assertEquals(3000, resultado.descripcion().length());
    }

    /**
     * Prueba la creación de PQR con fecha de respuesta
     * Verifica que se maneja correctamente cuando hay fecha de respuesta
     */
    @Test
    void testCrearPqr_ConFechaRespuesta() {
        // Arrange
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaRespuesta = LocalDateTime.now().plusDays(1);

        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                fechaRespuesta,
                EstadoPqr.RESUELTO);

        PQR pqr = new PQR();
        pqr.setIdPqr("pqr123");
        pqr.setFechaCreacion(fechaCreacion);
        pqr.setFechaRespuesta(fechaRespuesta);
        pqr.setEstadoPqr(EstadoPqr.RESUELTO);
        when(pqrRepository.save(any(PQR.class))).thenReturn(pqr);

        // Act
        PqrResponseDTO resultado = pqrService.crearPqr(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(fechaCreacion, resultado.fechaCreacion());
        assertEquals(fechaRespuesta, resultado.fechaRespuesta());
        assertEquals(EstadoPqr.RESUELTO, resultado.estadoPqr());
    }
}
