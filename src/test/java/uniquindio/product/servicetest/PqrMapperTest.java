package uniquindio.product.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;
import uniquindio.product.mapper.PqrMapper;
import uniquindio.product.model.documents.PQR;
import uniquindio.product.model.enums.CategoriaPqr;
import uniquindio.product.model.enums.EstadoPqr;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas unitarias para PqrMapper
 * Prueba todas las conversiones entre DTOs y entidades
 */
public class PqrMapperTest {

    private CrearPqrDTO crearPqrDTO;
    private PqrResponseDTO pqrResponseDTO;
    private PQR pqrEntity;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRespuesta;

    @BeforeEach
    void setUp() {
        fechaCreacion = LocalDateTime.now();
        fechaRespuesta = LocalDateTime.now().plusDays(1);

        crearPqrDTO = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                fechaRespuesta,
                EstadoPqr.ABIERTO);

        pqrResponseDTO = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                fechaRespuesta,
                EstadoPqr.ABIERTO);

        pqrEntity = new PQR();
        pqrEntity.setIdPqr("pqr123");
        pqrEntity.setIdUsuario("usuario123");
        pqrEntity.setCategoria(CategoriaPqr.RECLAMO);
        pqrEntity.setDescripcion("Descripción del reclamo");
        pqrEntity.setIdWorker("worker456");
        pqrEntity.setFechaCreacion(fechaCreacion);
        pqrEntity.setFechaRespuesta(fechaRespuesta);
        pqrEntity.setEstadoPqr(EstadoPqr.ABIERTO);
    }

    /**
     * Prueba la conversión de CrearPqrDTO a entidad PQR
     * Verifica que todos los campos se mapean correctamente
     */
    @Test
    void testToEntity_DesdeCrearPqrDTO() {
        // Act
        PQR resultado = PqrMapper.toEntity(crearPqrDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(crearPqrDTO.idUsuario(), resultado.getIdUsuario());
        assertEquals(crearPqrDTO.categoriaPqr(), resultado.getCategoria());
        assertEquals(crearPqrDTO.descripcion(), resultado.getDescripcion());
        assertEquals(crearPqrDTO.idWorker(), resultado.getIdWorker());
        assertEquals(crearPqrDTO.fechaCreacion(), resultado.getFechaCreacion());
        assertEquals(crearPqrDTO.fechaRespuesta(), resultado.getFechaRespuesta());
        assertEquals(crearPqrDTO.estadoPqr(), resultado.getEstadoPqr());
        assertNull(resultado.getIdPqr()); // No se establece en CrearPqrDTO
    }

    /**
     * Prueba la conversión de PqrResponseDTO a entidad PQR
     * Verifica que todos los campos se mapean correctamente
     */
    @Test
    void testToEntity_DesdePqrResponseDTO() {
        // Act
        PQR resultado = PqrMapper.toEntity(pqrResponseDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(pqrResponseDTO.idPqr(), resultado.getIdPqr());
        assertEquals(pqrResponseDTO.idUsuario(), resultado.getIdUsuario());
        assertEquals(pqrResponseDTO.categoriaPqr(), resultado.getCategoria());
        assertEquals(pqrResponseDTO.descripcion(), resultado.getDescripcion());
        assertEquals(pqrResponseDTO.idWorker(), resultado.getIdWorker());
        assertEquals(pqrResponseDTO.fechaCreacion(), resultado.getFechaCreacion());
        assertEquals(pqrResponseDTO.fechaRespuesta(), resultado.getFechaRespuesta());
        assertEquals(pqrResponseDTO.estadoPqr(), resultado.getEstadoPqr());
    }

    /**
     * Prueba la conversión de entidad PQR a PqrResponseDTO
     * Verifica que todos los campos se mapean correctamente
     */
    @Test
    void testToResponseDTO() {
        // Act
        PqrResponseDTO resultado = PqrMapper.toResponseDTO(pqrEntity);

        // Assert
        assertNotNull(resultado);
        assertEquals(pqrEntity.getIdPqr(), resultado.idPqr());
        assertEquals(pqrEntity.getIdUsuario(), resultado.idUsuario());
        assertEquals(pqrEntity.getCategoria(), resultado.categoriaPqr());
        assertEquals(pqrEntity.getDescripcion(), resultado.descripcion());
        assertEquals(pqrEntity.getIdWorker(), resultado.idWorker());
        assertEquals(pqrEntity.getFechaCreacion(), resultado.fechaCreacion());
        assertEquals(pqrEntity.getFechaRespuesta(), resultado.fechaRespuesta());
        assertEquals(pqrEntity.getEstadoPqr(), resultado.estadoPqr());
    }

    /**
     * Prueba la conversión con valores nulos en CrearPqrDTO
     * Verifica que se manejan correctamente los valores nulos
     */
    @Test
    void testToEntity_DesdeCrearPqrDTO_ConValoresNulos() {
        // Arrange
        CrearPqrDTO dtoConNulos = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                null, // idWorker nulo
                fechaCreacion,
                null, // fechaRespuesta nula
                EstadoPqr.ABIERTO);

        // Act
        PQR resultado = PqrMapper.toEntity(dtoConNulos);

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario123", resultado.getIdUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.getCategoria());
        assertEquals("Descripción del reclamo", resultado.getDescripcion());
        assertNull(resultado.getIdWorker());
        assertEquals(fechaCreacion, resultado.getFechaCreacion());
        assertNull(resultado.getFechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, resultado.getEstadoPqr());
    }

    /**
     * Prueba la conversión con valores nulos en PqrResponseDTO
     * Verifica que se manejan correctamente los valores nulos
     */
    @Test
    void testToEntity_DesdePqrResponseDTO_ConValoresNulos() {
        // Arrange
        PqrResponseDTO dtoConNulos = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                null, // idWorker nulo
                fechaCreacion,
                null, // fechaRespuesta nula
                EstadoPqr.ABIERTO);

        // Act
        PQR resultado = PqrMapper.toEntity(dtoConNulos);

        // Assert
        assertNotNull(resultado);
        assertEquals("pqr123", resultado.getIdPqr());
        assertEquals("usuario123", resultado.getIdUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.getCategoria());
        assertEquals("Descripción del reclamo", resultado.getDescripcion());
        assertNull(resultado.getIdWorker());
        assertEquals(fechaCreacion, resultado.getFechaCreacion());
        assertNull(resultado.getFechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, resultado.getEstadoPqr());
    }

    /**
     * Prueba la conversión de entidad con valores nulos a PqrResponseDTO
     * Verifica que se manejan correctamente los valores nulos
     */
    @Test
    void testToResponseDTO_ConValoresNulos() {
        // Arrange
        PQR pqrConNulos = new PQR();
        pqrConNulos.setIdPqr("pqr123");
        pqrConNulos.setIdUsuario("usuario123");
        pqrConNulos.setCategoria(CategoriaPqr.RECLAMO);
        pqrConNulos.setDescripcion("Descripción del reclamo");
        pqrConNulos.setIdWorker(null);
        pqrConNulos.setFechaCreacion(fechaCreacion);
        pqrConNulos.setFechaRespuesta(null);
        pqrConNulos.setEstadoPqr(EstadoPqr.ABIERTO);

        // Act
        PqrResponseDTO resultado = PqrMapper.toResponseDTO(pqrConNulos);

        // Assert
        assertNotNull(resultado);
        assertEquals("pqr123", resultado.idPqr());
        assertEquals("usuario123", resultado.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.categoriaPqr());
        assertEquals("Descripción del reclamo", resultado.descripcion());
        assertNull(resultado.idWorker());
        assertEquals(fechaCreacion, resultado.fechaCreacion());
        assertNull(resultado.fechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, resultado.estadoPqr());
    }

    /**
     * Prueba la conversión con todas las categorías de PQR
     * Verifica que se mapean correctamente todas las categorías
     */
    @Test
    void testToEntity_ConTodasLasCategorias() {
        CategoriaPqr[] categorias = {
                CategoriaPqr.SERVICIO_CLIENTE,
                CategoriaPqr.RECLAMO,
                CategoriaPqr.FACTURACION,
                CategoriaPqr.CUPON,
                CategoriaPqr.OTROS
        };

        for (CategoriaPqr categoria : categorias) {
            // Arrange
            CrearPqrDTO dto = new CrearPqrDTO(
                    "usuario123",
                    categoria,
                    "Descripción de prueba",
                    "worker456",
                    fechaCreacion,
                    null,
                    EstadoPqr.ABIERTO);

            // Act
            PQR resultado = PqrMapper.toEntity(dto);

            // Assert
            assertNotNull(resultado);
            assertEquals(categoria, resultado.getCategoria());
        }
    }

    /**
     * Prueba la conversión con todos los estados de PQR
     * Verifica que se mapean correctamente todos los estados
     */
    @Test
    void testToEntity_ConTodosLosEstados() {
        EstadoPqr[] estados = {
                EstadoPqr.ABIERTO,
                EstadoPqr.CERRADO,
                EstadoPqr.RESUELTO,
                EstadoPqr.ABANDONADO
        };

        for (EstadoPqr estado : estados) {
            // Arrange
            CrearPqrDTO dto = new CrearPqrDTO(
                    "usuario123",
                    CategoriaPqr.RECLAMO,
                    "Descripción de prueba",
                    "worker456",
                    fechaCreacion,
                    null,
                    estado);

            // Act
            PQR resultado = PqrMapper.toEntity(dto);

            // Assert
            assertNotNull(resultado);
            assertEquals(estado, resultado.getEstadoPqr());
        }
    }

    /**
     * Prueba la conversión con descripción de longitud máxima
     * Verifica que se maneja correctamente la descripción de 3000 caracteres
     */
    @Test
    void testToEntity_ConDescripcionLarga() {
        // Arrange
        String descripcionLarga = "A".repeat(3000);
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionLarga,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Act
        PQR resultado = PqrMapper.toEntity(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionLarga, resultado.getDescripcion());
        assertEquals(3000, resultado.getDescripcion().length());
    }

    /**
     * Prueba la conversión con fechas específicas
     * Verifica que se mapean correctamente las fechas
     */
    @Test
    void testToEntity_ConFechasEspecificas() {
        // Arrange
        LocalDateTime fechaEspecifica = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime fechaRespuestaEspecifica = LocalDateTime.of(2024, 1, 16, 14, 45);

        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaEspecifica,
                fechaRespuestaEspecifica,
                EstadoPqr.RESUELTO);

        // Act
        PQR resultado = PqrMapper.toEntity(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(fechaEspecifica, resultado.getFechaCreacion());
        assertEquals(fechaRespuestaEspecifica, resultado.getFechaRespuesta());
        assertEquals(EstadoPqr.RESUELTO, resultado.getEstadoPqr());
    }

    /**
     * Prueba la conversión bidireccional (round-trip)
     * Verifica que convertir de DTO a entidad y de vuelta a DTO mantiene la
     * información
     */
    @Test
    void testConversionBidireccional() {
        // Act - CrearPqrDTO -> PQR -> PqrResponseDTO
        PQR pqr = PqrMapper.toEntity(crearPqrDTO);
        PqrResponseDTO resultado = PqrMapper.toResponseDTO(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(crearPqrDTO.idUsuario(), resultado.idUsuario());
        assertEquals(crearPqrDTO.categoriaPqr(), resultado.categoriaPqr());
        assertEquals(crearPqrDTO.descripcion(), resultado.descripcion());
        assertEquals(crearPqrDTO.idWorker(), resultado.idWorker());
        assertEquals(crearPqrDTO.fechaCreacion(), resultado.fechaCreacion());
        assertEquals(crearPqrDTO.fechaRespuesta(), resultado.fechaRespuesta());
        assertEquals(crearPqrDTO.estadoPqr(), resultado.estadoPqr());
    }
}
