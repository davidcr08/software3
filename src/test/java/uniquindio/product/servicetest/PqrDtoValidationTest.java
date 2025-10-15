package uniquindio.product.servicetest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniquindio.product.dto.pqr.CrearPqrDTO;
import uniquindio.product.dto.pqr.PqrResponseDTO;
import uniquindio.product.model.enums.CategoriaPqr;
import uniquindio.product.model.enums.EstadoPqr;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para validación de DTOs PQR
 * Prueba la creación y validación de los DTOs de PQR
 */
public class PqrDtoValidationTest {

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaRespuesta;

    @BeforeEach
    void setUp() {
        fechaCreacion = LocalDateTime.now();
        fechaRespuesta = LocalDateTime.now().plusDays(1);
    }

    /**
     * Prueba la creación de CrearPqrDTO válido
     * Verifica que se crea correctamente con todos los campos
     */
    @Test
    void testCrearPqrDTO_Valido() {
        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                fechaRespuesta,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("usuario123", dto.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, dto.categoriaPqr());
        assertEquals("Descripción del reclamo", dto.descripcion());
        assertEquals("worker456", dto.idWorker());
        assertEquals(fechaCreacion, dto.fechaCreacion());
        assertEquals(fechaRespuesta, dto.fechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, dto.estadoPqr());
    }

    /**
     * Prueba la creación de CrearPqrDTO con valores nulos
     * Verifica que se aceptan valores nulos en campos opcionales
     */
    @Test
    void testCrearPqrDTO_ConValoresNulos() {
        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                null, // idWorker nulo
                fechaCreacion,
                null, // fechaRespuesta nula
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("usuario123", dto.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, dto.categoriaPqr());
        assertEquals("Descripción del reclamo", dto.descripcion());
        assertNull(dto.idWorker());
        assertEquals(fechaCreacion, dto.fechaCreacion());
        assertNull(dto.fechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, dto.estadoPqr());
    }

    /**
     * Prueba la creación de CrearPqrDTO con todas las categorías
     * Verifica que se aceptan todas las categorías del enum
     */
    @Test
    void testCrearPqrDTO_TodasLasCategorias() {
        CategoriaPqr[] categorias = {
                CategoriaPqr.SERVICIO_CLIENTE,
                CategoriaPqr.RECLAMO,
                CategoriaPqr.FACTURACION,
                CategoriaPqr.CUPON,
                CategoriaPqr.OTROS
        };

        for (CategoriaPqr categoria : categorias) {
            // Act
            CrearPqrDTO dto = new CrearPqrDTO(
                    "usuario123",
                    categoria,
                    "Descripción de prueba",
                    "worker456",
                    fechaCreacion,
                    null,
                    EstadoPqr.ABIERTO);

            // Assert
            assertNotNull(dto);
            assertEquals(categoria, dto.categoriaPqr());
        }
    }

    /**
     * Prueba la creación de CrearPqrDTO con todos los estados
     * Verifica que se aceptan todos los estados del enum
     */
    @Test
    void testCrearPqrDTO_TodosLosEstados() {
        EstadoPqr[] estados = {
                EstadoPqr.ABIERTO,
                EstadoPqr.CERRADO,
                EstadoPqr.RESUELTO,
                EstadoPqr.ABANDONADO
        };

        for (EstadoPqr estado : estados) {
            // Act
            CrearPqrDTO dto = new CrearPqrDTO(
                    "usuario123",
                    CategoriaPqr.RECLAMO,
                    "Descripción de prueba",
                    "worker456",
                    fechaCreacion,
                    null,
                    estado);

            // Assert
            assertNotNull(dto);
            assertEquals(estado, dto.estadoPqr());
        }
    }

    /**
     * Prueba la creación de CrearPqrDTO con descripción larga
     * Verifica que se acepta descripción de 3000 caracteres
     */
    @Test
    void testCrearPqrDTO_DescripcionLarga() {
        // Arrange
        String descripcionLarga = "A".repeat(3000);

        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionLarga,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionLarga, dto.descripcion());
        assertEquals(3000, dto.descripcion().length());
    }

    /**
     * Prueba la creación de CrearPqrDTO con descripción vacía
     * Verifica que se acepta descripción vacía (la validación se hace en la
     * entidad)
     */
    @Test
    void testCrearPqrDTO_DescripcionVacia() {
        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "",
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.descripcion());
    }

    /**
     * Prueba la creación de CrearPqrDTO con idUsuario vacío
     * Verifica que se acepta idUsuario vacío (la validación se hace en la entidad)
     */
    @Test
    void testCrearPqrDTO_IdUsuarioVacio() {
        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.idUsuario());
    }

    /**
     * Prueba la creación de CrearPqrDTO con idWorker vacío
     * Verifica que se acepta idWorker vacío (es opcional)
     */
    @Test
    void testCrearPqrDTO_IdWorkerVacio() {
        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.idWorker());
    }

    /**
     * Prueba la creación de CrearPqrDTO con fechas específicas
     * Verifica que se aceptan fechas específicas
     */
    @Test
    void testCrearPqrDTO_FechasEspecificas() {
        // Arrange
        LocalDateTime fechaEspecifica = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime fechaRespuestaEspecifica = LocalDateTime.of(2024, 1, 16, 14, 45);

        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaEspecifica,
                fechaRespuestaEspecifica,
                EstadoPqr.RESUELTO);

        // Assert
        assertNotNull(dto);
        assertEquals(fechaEspecifica, dto.fechaCreacion());
        assertEquals(fechaRespuestaEspecifica, dto.fechaRespuesta());
        assertEquals(EstadoPqr.RESUELTO, dto.estadoPqr());
    }

    /**
     * Prueba la creación de CrearPqrDTO con descripción con caracteres especiales
     * Verifica que se acepta descripción con caracteres especiales
     */
    @Test
    void testCrearPqrDTO_DescripcionConCaracteresEspeciales() {
        // Arrange
        String descripcionEspecial = "Descripción con acentos y símbolos: @#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionEspecial,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionEspecial, dto.descripcion());
    }

    /**
     * Prueba la creación de CrearPqrDTO con descripción con saltos de línea
     * Verifica que se acepta descripción con saltos de línea
     */
    @Test
    void testCrearPqrDTO_DescripcionConSaltosLinea() {
        // Arrange
        String descripcionConSaltos = "Descripción con\nsaltos de línea\ny múltiples líneas";

        // Act
        CrearPqrDTO dto = new CrearPqrDTO(
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionConSaltos,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionConSaltos, dto.descripcion());
    }

    /**
     * Prueba la creación de PqrResponseDTO válido
     * Verifica que se crea correctamente con todos los campos
     */
    @Test
    void testPqrResponseDTO_Valido() {
        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                fechaRespuesta,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("pqr123", dto.idPqr());
        assertEquals("usuario123", dto.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, dto.categoriaPqr());
        assertEquals("Descripción del reclamo", dto.descripcion());
        assertEquals("worker456", dto.idWorker());
        assertEquals(fechaCreacion, dto.fechaCreacion());
        assertEquals(fechaRespuesta, dto.fechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, dto.estadoPqr());
    }

    /**
     * Prueba la creación de PqrResponseDTO con valores nulos
     * Verifica que se aceptan valores nulos en campos opcionales
     */
    @Test
    void testPqrResponseDTO_ConValoresNulos() {
        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                null, // idWorker nulo
                fechaCreacion,
                null, // fechaRespuesta nula
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("pqr123", dto.idPqr());
        assertEquals("usuario123", dto.idUsuario());
        assertEquals(CategoriaPqr.RECLAMO, dto.categoriaPqr());
        assertEquals("Descripción del reclamo", dto.descripcion());
        assertNull(dto.idWorker());
        assertEquals(fechaCreacion, dto.fechaCreacion());
        assertNull(dto.fechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, dto.estadoPqr());
    }

    /**
     * Prueba la creación de PqrResponseDTO con todas las categorías
     * Verifica que se aceptan todas las categorías del enum
     */
    @Test
    void testPqrResponseDTO_TodasLasCategorias() {
        CategoriaPqr[] categorias = {
                CategoriaPqr.SERVICIO_CLIENTE,
                CategoriaPqr.RECLAMO,
                CategoriaPqr.FACTURACION,
                CategoriaPqr.CUPON,
                CategoriaPqr.OTROS
        };

        for (CategoriaPqr categoria : categorias) {
            // Act
            PqrResponseDTO dto = new PqrResponseDTO(
                    "pqr123",
                    "usuario123",
                    categoria,
                    "Descripción de prueba",
                    "worker456",
                    fechaCreacion,
                    null,
                    EstadoPqr.ABIERTO);

            // Assert
            assertNotNull(dto);
            assertEquals(categoria, dto.categoriaPqr());
        }
    }

    /**
     * Prueba la creación de PqrResponseDTO con todos los estados
     * Verifica que se aceptan todos los estados del enum
     */
    @Test
    void testPqrResponseDTO_TodosLosEstados() {
        EstadoPqr[] estados = {
                EstadoPqr.ABIERTO,
                EstadoPqr.CERRADO,
                EstadoPqr.RESUELTO,
                EstadoPqr.ABANDONADO
        };

        for (EstadoPqr estado : estados) {
            // Act
            PqrResponseDTO dto = new PqrResponseDTO(
                    "pqr123",
                    "usuario123",
                    CategoriaPqr.RECLAMO,
                    "Descripción de prueba",
                    "worker456",
                    fechaCreacion,
                    null,
                    estado);

            // Assert
            assertNotNull(dto);
            assertEquals(estado, dto.estadoPqr());
        }
    }

    /**
     * Prueba la creación de PqrResponseDTO con descripción larga
     * Verifica que se acepta descripción de 3000 caracteres
     */
    @Test
    void testPqrResponseDTO_DescripcionLarga() {
        // Arrange
        String descripcionLarga = "A".repeat(3000);

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionLarga,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionLarga, dto.descripcion());
        assertEquals(3000, dto.descripcion().length());
    }

    /**
     * Prueba la creación de PqrResponseDTO con fechas específicas
     * Verifica que se aceptan fechas específicas
     */
    @Test
    void testPqrResponseDTO_FechasEspecificas() {
        // Arrange
        LocalDateTime fechaEspecifica = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime fechaRespuestaEspecifica = LocalDateTime.of(2024, 1, 16, 14, 45);

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaEspecifica,
                fechaRespuestaEspecifica,
                EstadoPqr.RESUELTO);

        // Assert
        assertNotNull(dto);
        assertEquals(fechaEspecifica, dto.fechaCreacion());
        assertEquals(fechaRespuestaEspecifica, dto.fechaRespuesta());
        assertEquals(EstadoPqr.RESUELTO, dto.estadoPqr());
    }

    /**
     * Prueba la creación de PqrResponseDTO con descripción con caracteres
     * especiales
     * Verifica que se acepta descripción con caracteres especiales
     */
    @Test
    void testPqrResponseDTO_DescripcionConCaracteresEspeciales() {
        // Arrange
        String descripcionEspecial = "Descripción con acentos y símbolos: @#$%^&*()_+-=[]{}|;':\",./<>?";

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionEspecial,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionEspecial, dto.descripcion());
    }

    /**
     * Prueba la creación de PqrResponseDTO con descripción con saltos de línea
     * Verifica que se acepta descripción con saltos de línea
     */
    @Test
    void testPqrResponseDTO_DescripcionConSaltosLinea() {
        // Arrange
        String descripcionConSaltos = "Descripción con\nsaltos de línea\ny múltiples líneas";

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionConSaltos,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionConSaltos, dto.descripcion());
    }

    /**
     * Prueba la creación de PqrResponseDTO con descripción con emojis
     * Verifica que se acepta descripción con emojis
     */
    @Test
    void testPqrResponseDTO_DescripcionConEmojis() {
        // Arrange
        String descripcionConEmojis = "Descripción con emojis 😀😁😂🤣😃😄😅😆😉😊😋😎😍😘🥰😗😙😚☺️🙂🤗🤩🤔🤨😐😑😶🙄😏😣😥😮🤐😯😪😫🥱😴😌😛😜😝🤤😒😓😔😕🙃🤑😲☹️🙁😖😞😟😤😢😭😦😧😨😩🤯😬😰😱🥵🥶😳🤪😵😡😠🤬😷🤒🤕🤢🤮🤧😇🤠🤡🥳🥴🤤🤫🤭🧐🤓😈👿";

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionConEmojis,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionConEmojis, dto.descripcion());
    }

    /**
     * Prueba la creación de PqrResponseDTO con descripción con HTML
     * Verifica que se acepta descripción con HTML
     */
    @Test
    void testPqrResponseDTO_DescripcionConHTML() {
        // Arrange
        String descripcionHTML = "<p>Descripción con <strong>HTML</strong> y <em>formato</em></p>";

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionHTML,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionHTML, dto.descripcion());
    }

    /**
     * Prueba la creación de PqrResponseDTO con descripción con SQL
     * Verifica que se acepta descripción con SQL
     */
    @Test
    void testPqrResponseDTO_DescripcionConSQL() {
        // Arrange
        String descripcionSQL = "SELECT * FROM usuarios WHERE id = '123'; DROP TABLE usuarios;";

        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                descripcionSQL,
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals(descripcionSQL, dto.descripcion());
    }

    /**
     * Prueba la creación de PqrResponseDTO con idPqr vacío
     * Verifica que se acepta idPqr vacío (la validación se hace en la entidad)
     */
    @Test
    void testPqrResponseDTO_IdPqrVacio() {
        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.idPqr());
    }

    /**
     * Prueba la creación de PqrResponseDTO con idUsuario vacío
     * Verifica que se acepta idUsuario vacío (la validación se hace en la entidad)
     */
    @Test
    void testPqrResponseDTO_IdUsuarioVacio() {
        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "worker456",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.idUsuario());
    }

    /**
     * Prueba la creación de PqrResponseDTO con idWorker vacío
     * Verifica que se acepta idWorker vacío (es opcional)
     */
    @Test
    void testPqrResponseDTO_IdWorkerVacio() {
        // Act
        PqrResponseDTO dto = new PqrResponseDTO(
                "pqr123",
                "usuario123",
                CategoriaPqr.RECLAMO,
                "Descripción del reclamo",
                "",
                fechaCreacion,
                null,
                EstadoPqr.ABIERTO);

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.idWorker());
    }
}
