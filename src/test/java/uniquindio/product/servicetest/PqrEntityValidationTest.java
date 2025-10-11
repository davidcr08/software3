package uniquindio.product.servicetest;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import uniquindio.product.model.documents.PQR;
import uniquindio.product.model.enums.CategoriaPqr;
import uniquindio.product.model.enums.EstadoPqr;

import jakarta.persistence.PersistenceException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas para validación de entidad PQR
 * Prueba las restricciones y validaciones de la entidad PQR
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PqrEntityValidationTest {

    @Autowired
    private TestEntityManager entityManager;

    private PQR pqr;

    @BeforeEach
    void setUp() {
        pqr = new PQR();
        pqr.setIdUsuario("usuario123");
        pqr.setCategoria(CategoriaPqr.RECLAMO);
        pqr.setDescripcion("Descripción del reclamo");
        pqr.setIdWorker("worker456");
        pqr.setFechaCreacion(LocalDateTime.now());
        pqr.setFechaRespuesta(null);
        pqr.setEstadoPqr(EstadoPqr.ABIERTO);
    }

    /**
     * Prueba la creación de un PQR válido
     * Verifica que se crea correctamente con todos los campos requeridos
     */
    @Test
    void testCrearPqr_Valido() {
        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getIdPqr());
        assertEquals("usuario123", resultado.getIdUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.getCategoria());
        assertEquals("Descripción del reclamo", resultado.getDescripcion());
        assertEquals("worker456", resultado.getIdWorker());
        assertNotNull(resultado.getFechaCreacion());
        assertNull(resultado.getFechaRespuesta());
        assertEquals(EstadoPqr.ABIERTO, resultado.getEstadoPqr());
    }

    /**
     * Prueba la validación de campo idUsuario requerido
     * Verifica que se lanza excepción cuando idUsuario es null
     */
    @Test
    void testValidacion_IdUsuarioRequerido() {
        // Arrange
        pqr.setIdUsuario(null);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }

    /**
     * Prueba la validación de campo categoria requerido
     * Verifica que se lanza excepción cuando categoria es null
     */
    @Test
    void testValidacion_CategoriaRequerida() {
        // Arrange
        pqr.setCategoria(null);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }

    /**
     * Prueba la validación de campo descripcion requerido
     * Verifica que se lanza excepción cuando descripcion es null
     */
    @Test
    void testValidacion_DescripcionRequerida() {
        // Arrange
        pqr.setDescripcion(null);

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }

    /**
     * Prueba la validación de campo fechaCreacion requerido
     * Verifica que se lanza excepción cuando fechaCreacion es null
     */
    @Test
    void testValidacion_FechaCreacionRequerida() {
        // Arrange
        pqr.setFechaCreacion(null);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }

    /**
     * Prueba la validación de campo estadoPqr requerido
     * Verifica que se lanza excepción cuando estadoPqr es null
     */
    @Test
    void testValidacion_EstadoPqrRequerido() {
        // Arrange
        pqr.setEstadoPqr(null);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }

    /**
     * Prueba la validación de longitud máxima de descripción
     * Verifica que se acepta descripción de 3000 caracteres
     */
    @Test
    void testValidacion_DescripcionLongitudMaxima() {
        // Arrange
        String descripcionLarga = "A".repeat(3000);
        pqr.setDescripcion(descripcionLarga);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionLarga, resultado.getDescripcion());
        assertEquals(3000, resultado.getDescripcion().length());
    }

    /**
     * Prueba la validación de longitud excesiva de descripción
     * Verifica que se lanza excepción cuando descripción excede 3000 caracteres
     */
    @Test
    void testValidacion_DescripcionLongitudExcesiva() {
        // Arrange
        String descripcionExcesiva = "A".repeat(3001);
        pqr.setDescripcion(descripcionExcesiva);

        // Act & Assert
        assertThrows(PersistenceException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }

    /**
     * Prueba la validación de campo idWorker opcional
     * Verifica que se acepta idWorker como null
     */
    @Test
    void testValidacion_IdWorkerOpcional() {
        // Arrange
        pqr.setIdWorker(null);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getIdWorker());
    }

    /**
     * Prueba la validación de campo fechaRespuesta opcional
     * Verifica que se acepta fechaRespuesta como null
     */
    @Test
    void testValidacion_FechaRespuestaOpcional() {
        // Arrange
        pqr.setFechaRespuesta(null);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertNull(resultado.getFechaRespuesta());
    }

    /**
     * Prueba la validación de todas las categorías válidas
     * Verifica que se aceptan todas las categorías del enum
     */
    @Test
    void testValidacion_TodasLasCategorias() {
        CategoriaPqr[] categorias = {
                CategoriaPqr.SERVICIO_CLIENTE,
                CategoriaPqr.RECLAMO,
                CategoriaPqr.FACTURACION,
                CategoriaPqr.CUPON,
                CategoriaPqr.OTROS
        };

        for (CategoriaPqr categoria : categorias) {
            // Arrange: crear un nuevo PQR para cada iteración
            PQR nuevoPqr = new PQR();
            nuevoPqr.setDescripcion("Test para categoría " + categoria);
            nuevoPqr.setCategoria(categoria);
            nuevoPqr.setEstadoPqr(EstadoPqr.ABIERTO);
            nuevoPqr.setIdUsuario("usuario123");
            nuevoPqr.setIdWorker("worker456");
            nuevoPqr.setFechaCreacion(LocalDateTime.now());

            // Act
            PQR resultado = entityManager.persistAndFlush(nuevoPqr);

            // Assert
            assertNotNull(resultado);
            assertEquals(categoria, resultado.getCategoria());
        }
    }


    /**
     * Prueba la validación de todos los estados válidos
     * Verifica que se aceptan todos los estados del enum
     */
    @Test
    void testValidacion_TodosLosEstados() {
        EstadoPqr[] estados = {
                EstadoPqr.ABIERTO,
                EstadoPqr.CERRADO,
                EstadoPqr.RESUELTO,
                EstadoPqr.ABANDONADO
        };

        for (EstadoPqr estado : estados) {
            PQR nuevoPqr = new PQR();
            nuevoPqr.setDescripcion("Test " + estado);
            nuevoPqr.setCategoria(CategoriaPqr.RECLAMO);
            nuevoPqr.setIdUsuario("usuario123");
            nuevoPqr.setIdWorker("worker456");
            nuevoPqr.setEstadoPqr(estado);
            nuevoPqr.setFechaCreacion(LocalDateTime.now()); // 👈 necesario

            PQR resultado = entityManager.persistAndFlush(nuevoPqr);

            assertNotNull(resultado);
            assertEquals(estado, resultado.getEstadoPqr());
        }
    }


    /**
     * Prueba la validación de fechas válidas
     * Verifica que se aceptan fechas válidas
     */
    @Test
    void testValidacion_FechasValidas() {
        // Arrange
        LocalDateTime fechaCreacion = LocalDateTime.of(2024, 1, 15, 10, 30);
        LocalDateTime fechaRespuesta = LocalDateTime.of(2024, 1, 16, 14, 45);

        pqr.setFechaCreacion(fechaCreacion);
        pqr.setFechaRespuesta(fechaRespuesta);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(fechaCreacion, resultado.getFechaCreacion());
        assertEquals(fechaRespuesta, resultado.getFechaRespuesta());
    }

    /**
     * Prueba la validación de fecha de respuesta posterior a fecha de creación
     * Verifica que se acepta fecha de respuesta posterior a fecha de creación
     */
    @Test
    void testValidacion_FechaRespuestaPosterior() {
        // Arrange
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaRespuesta = fechaCreacion.plusDays(1);

        pqr.setFechaCreacion(fechaCreacion);
        pqr.setFechaRespuesta(fechaRespuesta);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(fechaCreacion, resultado.getFechaCreacion());
        assertEquals(fechaRespuesta, resultado.getFechaRespuesta());
        assertTrue(resultado.getFechaRespuesta().isAfter(resultado.getFechaCreacion()));
    }

    /**
     * Prueba la validación de descripción vacía
     * Verifica que se lanza excepción cuando descripción está vacía
     */
    @Test
    void testValidacion_DescripcionVacia() {
        // Arrange
        pqr.setDescripcion("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }


    /**
     * Prueba la validación de descripción con solo espacios
     * Verifica que se lanza excepción cuando descripción contiene solo espacios
     */
    @Test
    void testValidacion_DescripcionSoloEspacios() {
        // Arrange
        pqr.setDescripcion("   ");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> {
            entityManager.persistAndFlush(pqr);
        });
    }





    /**
     * Prueba la validación de idWorker vacío
     * Verifica que se acepta idWorker vacío (es opcional)
     */
    @Test
    void testValidacion_IdWorkerVacio() {
        // Arrange
        pqr.setIdWorker("");

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals("", resultado.getIdWorker());
    }

    /**
     * Prueba la validación de idWorker con solo espacios
     * Verifica que se acepta idWorker con solo espacios (es opcional)
     */
    @Test
    void testValidacion_IdWorkerSoloEspacios() {
        // Arrange
        pqr.setIdWorker("   ");

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals("   ", resultado.getIdWorker());
    }

    /**
     * Prueba la validación de descripción con caracteres especiales
     * Verifica que se acepta descripción con caracteres especiales
     */
    @Test
    void testValidacion_DescripcionConCaracteresEspeciales() {
        // Arrange
        String descripcionEspecial = "Descripción con acentos y símbolos: @#$%^&*()_+-=[]{}|;':\",./<>?";
        pqr.setDescripcion(descripcionEspecial);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionEspecial, resultado.getDescripcion());
    }

    /**
     * Prueba la validación de descripción con saltos de línea
     * Verifica que se acepta descripción con saltos de línea
     */
    @Test
    void testValidacion_DescripcionConSaltosLinea() {
        // Arrange
        String descripcionConSaltos = "Descripción con\nsaltos de línea\ny múltiples líneas";
        pqr.setDescripcion(descripcionConSaltos);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionConSaltos, resultado.getDescripcion());
    }

    /**
     * Prueba la validación de descripción con emojis
     * Verifica que se acepta descripción con emojis
     */
    @Test
    void testValidacion_DescripcionConEmojis() {
        // Arrange
        String descripcionConEmojis = "Descripción con emojis 😀😁😂🤣😃😄😅😆😉😊😋😎😍😘🥰😗😙😚☺️🙂🤗🤩🤔🤨😐😑😶🙄😏😣😥😮🤐😯😪😫🥱😴😌😛😜😝🤤😒😓😔😕🙃🤑😲☹️🙁😖😞😟😤😢😭😦😧😨😩🤯😬😰😱🥵🥶😳🤪😵😡😠🤬😷🤒🤕🤢🤮🤧😇🤠🤡🥳🥴🤤🤫🤭🧐🤓😈👿";
        pqr.setDescripcion(descripcionConEmojis);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionConEmojis, resultado.getDescripcion());
    }

    /**
     * Prueba la validación de descripción con HTML
     * Verifica que se acepta descripción con HTML
     */
    @Test
    void testValidacion_DescripcionConHTML() {
        // Arrange
        String descripcionHTML = "<p>Descripción con <strong>HTML</strong> y <em>formato</em></p>";
        pqr.setDescripcion(descripcionHTML);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionHTML, resultado.getDescripcion());
    }

    /**
     * Prueba la validación de descripción con SQL
     * Verifica que se acepta descripción con SQL (no se valida contenido)
     */
    @Test
    void testValidacion_DescripcionConSQL() {
        // Arrange
        String descripcionSQL = "SELECT * FROM usuarios WHERE id = '123'; DROP TABLE usuarios;";
        pqr.setDescripcion(descripcionSQL);

        // Act
        PQR resultado = entityManager.persistAndFlush(pqr);

        // Assert
        assertNotNull(resultado);
        assertEquals(descripcionSQL, resultado.getDescripcion());
    }
}
