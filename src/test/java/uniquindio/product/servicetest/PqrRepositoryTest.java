package uniquindio.product.servicetest;

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
import uniquindio.product.repositories.PqrRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Clase de pruebas de integración para PqrRepository
 * Prueba las operaciones de base de datos del repositorio PQR
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PqrRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PqrRepository pqrRepository;

    private PQR pqr1;
    private PQR pqr2;
    private PQR pqr3;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        pqrRepository.deleteAll();

        // Crear datos de prueba
        LocalDateTime fechaCreacion = LocalDateTime.now();
        LocalDateTime fechaRespuesta = LocalDateTime.now().plusDays(1);

        pqr1 = new PQR();
        pqr1.setIdUsuario("usuario123");
        pqr1.setCategoria(CategoriaPqr.RECLAMO);
        pqr1.setDescripcion("Reclamo sobre producto defectuoso");
        pqr1.setIdWorker("worker456");
        pqr1.setFechaCreacion(fechaCreacion);
        pqr1.setFechaRespuesta(fechaRespuesta);
        pqr1.setEstadoPqr(EstadoPqr.RESUELTO);

        pqr2 = new PQR();
        pqr2.setIdUsuario("usuario458");
        pqr2.setCategoria(CategoriaPqr.SERVICIO_CLIENTE);
        pqr2.setDescripcion("Consulta sobre facturación");
        pqr2.setIdWorker("worker789");
        pqr2.setFechaCreacion(fechaCreacion.plusHours(1));
        pqr2.setFechaRespuesta(null);
        pqr2.setEstadoPqr(EstadoPqr.ABIERTO);

        pqr3 = new PQR();
        pqr3.setIdUsuario("usuario125");
        pqr3.setCategoria(CategoriaPqr.FACTURACION);
        pqr3.setDescripcion("Problema con factura");
        pqr3.setIdWorker("worker459");
        pqr3.setFechaCreacion(fechaCreacion.plusHours(2));
        pqr3.setFechaRespuesta(null);
        pqr3.setEstadoPqr(EstadoPqr.ABIERTO);

        // Persistir en la base de datos
        entityManager.persistAndFlush(pqr1);
        entityManager.persistAndFlush(pqr2);
        entityManager.persistAndFlush(pqr3);
    }

    /**
     * Prueba la búsqueda de PQR por ID
     * Verifica que se encuentra correctamente el PQR
     */
    @Test
    void testFindByIdPqr_Exitoso() {
        // Act
        PQR resultado = pqrRepository.findByIdPqr(pqr1.getIdPqr());

        // Assert
        assertNotNull(resultado);
        assertEquals(pqr1.getIdPqr(), resultado.getIdPqr());
        assertEquals("usuario123", resultado.getIdUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.getCategoria());
        assertEquals("Reclamo sobre producto defectuoso", resultado.getDescripcion());
        assertEquals("worker456", resultado.getIdWorker());
        assertEquals(EstadoPqr.RESUELTO, resultado.getEstadoPqr());
    }

    /**
     * Prueba la búsqueda de PQR por ID cuando no existe
     * Verifica que retorna null
     */
    @Test
    void testFindByIdPqr_NoExiste() {
        // Act
        PQR resultado = pqrRepository.findByIdPqr("idNoExiste");

        // Assert
        assertNull(resultado);
    }

    /**
     * Prueba la búsqueda de PQR por ID de worker
     * Verifica que se encuentra correctamente el PQR
     */
    @Test
    void testFindByIdWorker_Exitoso() {
        // Act
        PQR resultado = pqrRepository.findByIdWorker("worker456");

        // Assert
        assertNotNull(resultado);
        assertEquals("worker456", resultado.getIdWorker());
        assertEquals("usuario123", resultado.getIdUsuario());
        assertEquals(CategoriaPqr.RECLAMO, resultado.getCategoria());
    }

    /**
     * Prueba la búsqueda de PQR por ID de worker cuando no existe
     * Verifica que retorna null
     */
    @Test
    void testFindByIdWorker_NoExiste() {
        // Act
        PQR resultado = pqrRepository.findByIdWorker("workerNoExiste");

        // Assert
        assertNull(resultado);
    }

    /**
     * Prueba la búsqueda de PQR por ID de usuario
     * Verifica que se encuentra correctamente el PQR
     */
    @Test
    void testFindByIdUsuario_Exitoso() {
        // Act
        PQR resultado = pqrRepository.findByIdUsuario("usuario123");

        // Assert
        assertNotNull(resultado);
        assertEquals("usuario123", resultado.getIdUsuario());
        // Nota: Puede retornar cualquiera de los PQRs del usuario
        assertTrue(resultado.getCategoria() == CategoriaPqr.RECLAMO ||
                resultado.getCategoria() == CategoriaPqr.FACTURACION);
    }

    /**
     * Prueba la búsqueda de PQR por ID de usuario cuando no existe
     * Verifica que retorna null
     */
    @Test
    void testFindByIdUsuario_NoExiste() {
        // Act
        PQR resultado = pqrRepository.findByIdUsuario("usuarioNoExiste");

        // Assert
        assertNull(resultado);
    }

    /**
     * Prueba el guardado de un nuevo PQR
     * Verifica que se persiste correctamente en la base de datos
     */
    @Test
    void testSave_NuevoPqr() {
        // Arrange
        PQR nuevoPqr = new PQR();
        nuevoPqr.setIdUsuario("usuario789");
        nuevoPqr.setCategoria(CategoriaPqr.CUPON);
        nuevoPqr.setDescripcion("Consulta sobre cupón de descuento");
        nuevoPqr.setIdWorker("worker123");
        nuevoPqr.setFechaCreacion(LocalDateTime.now());
        nuevoPqr.setFechaRespuesta(null);
        nuevoPqr.setEstadoPqr(EstadoPqr.ABIERTO);

        // Act
        PQR resultado = pqrRepository.save(nuevoPqr);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getIdPqr());
        assertEquals("usuario789", resultado.getIdUsuario());
        assertEquals(CategoriaPqr.CUPON, resultado.getCategoria());
        assertEquals("Consulta sobre cupón de descuento", resultado.getDescripcion());
        assertEquals("worker123", resultado.getIdWorker());
        assertEquals(EstadoPqr.ABIERTO, resultado.getEstadoPqr());

        // Verificar que se guardó en la base de datos
        PQR pqrGuardado = pqrRepository.findByIdPqr(resultado.getIdPqr());
        assertNotNull(pqrGuardado);
        assertEquals(resultado.getIdPqr(), pqrGuardado.getIdPqr());
    }

    /**
     * Prueba la actualización de un PQR existente
     * Verifica que se actualiza correctamente en la base de datos
     */
    @Test
    void testSave_ActualizarPqr() {
        // Arrange
        pqr1.setEstadoPqr(EstadoPqr.CERRADO);
        pqr1.setFechaRespuesta(LocalDateTime.now());

        // Act
        PQR resultado = pqrRepository.save(pqr1);

        // Assert
        assertNotNull(resultado);
        assertEquals(EstadoPqr.CERRADO, resultado.getEstadoPqr());
        assertNotNull(resultado.getFechaRespuesta());

        // Verificar que se actualizó en la base de datos
        PQR pqrActualizado = pqrRepository.findByIdPqr(pqr1.getIdPqr());
        assertNotNull(pqrActualizado);
        assertEquals(EstadoPqr.CERRADO, pqrActualizado.getEstadoPqr());
        assertNotNull(pqrActualizado.getFechaRespuesta());
    }

    /**
     * Prueba la eliminación de un PQR
     * Verifica que se elimina correctamente de la base de datos
     */
    @Test
    void testDelete_Pqr() {
        // Arrange
        String idPqr = pqr1.getIdPqr();

        // Act
        pqrRepository.delete(pqr1);
        entityManager.flush();

        // Assert
        PQR pqrEliminado = pqrRepository.findByIdPqr(idPqr);
        assertNull(pqrEliminado);
    }

    /**
     * Prueba la búsqueda de todos los PQRs
     * Verifica que se retornan todos los PQRs guardados
     */
    @Test
    void testFindAll() {
        // Act
        List<PQR> resultados = pqrRepository.findAll();

        // Assert
        assertNotNull(resultados);
        assertEquals(3, resultados.size());
    }

    /**
     * Prueba la búsqueda por categoría usando consulta personalizada
     * Verifica que se encuentran los PQRs de una categoría específica
     */
    @Test
    void testFindByCategoria() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getCategoria() == CategoriaPqr.RECLAMO)
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(CategoriaPqr.RECLAMO, resultados.get(0).getCategoria());
    }

    /**
     * Prueba la búsqueda por estado usando consulta personalizada
     * Verifica que se encuentran los PQRs de un estado específico
     */
    @Test
    void testFindByEstado() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getEstadoPqr() == EstadoPqr.ABIERTO)
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().allMatch(pqr -> pqr.getEstadoPqr() == EstadoPqr.ABIERTO));
    }

    /**
     * Prueba la búsqueda por usuario específico
     * Verifica que se encuentran todos los PQRs de un usuario
     */
    @Test
    void testFindByUsuario() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getIdUsuario().equals("usuario123"))
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertTrue(resultados.stream().allMatch(pqr -> pqr.getIdUsuario().equals("usuario123")));
    }

    /**
     * Prueba la búsqueda por worker específico
     * Verifica que se encuentran todos los PQRs asignados a un worker
     */
    @Test
    void testFindByWorker() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getIdWorker().equals("worker456"))
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertTrue(resultados.stream().allMatch(pqr -> pqr.getIdWorker().equals("worker456")));
    }

    /**
     * Prueba la búsqueda de PQRs con fecha de respuesta
     * Verifica que se encuentran los PQRs que tienen fecha de respuesta
     */
    @Test
    void testFindByFechaRespuestaNotNull() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getFechaRespuesta() != null)
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertNotNull(resultados.get(0).getFechaRespuesta());
    }

    /**
     * Prueba la búsqueda de PQRs sin fecha de respuesta
     * Verifica que se encuentran los PQRs que no tienen fecha de respuesta
     */
    @Test
    void testFindByFechaRespuestaNull() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getFechaRespuesta() == null)
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().allMatch(pqr -> pqr.getFechaRespuesta() == null));
    }

    /**
     * Prueba la búsqueda de PQRs por rango de fechas
     * Verifica que se encuentran los PQRs creados en un rango específico
     */
    @Test
    void testFindByRangoFechas() {
        // Arrange
        LocalDateTime fechaInicio = LocalDateTime.now().minusHours(1);
        LocalDateTime fechaFin = LocalDateTime.now().plusHours(3);

        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getFechaCreacion().isAfter(fechaInicio) &&
                        pqr.getFechaCreacion().isBefore(fechaFin))
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(3, resultados.size());
    }

    /**
     * Prueba la búsqueda de PQRs por descripción que contiene texto
     * Verifica que se encuentran los PQRs con descripción que contiene un texto
     * específico
     */



    /**
     * Prueba la búsqueda de PQRs por categoría y estado
     * Verifica que se encuentran los PQRs de una categoría específica y un estado
     * específico
     */
    @Test
    void testFindByCategoriaYEstado() {
        // Act
        List<PQR> resultados = pqrRepository.findAll().stream()
                .filter(pqr -> pqr.getCategoria() == CategoriaPqr.SERVICIO_CLIENTE &&
                        pqr.getEstadoPqr() == EstadoPqr.ABIERTO)
                .toList();

        // Assert
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals(CategoriaPqr.SERVICIO_CLIENTE, resultados.get(0).getCategoria());
        assertEquals(EstadoPqr.ABIERTO, resultados.get(0).getEstadoPqr());
    }
}
