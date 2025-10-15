package uniquindio.product.servicetest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uniquindio.product.model.documents.Bodega;
import uniquindio.product.repositories.BodegaRepository;
import uniquindio.product.services.implementations.BodegaServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BodegaServiceImplTest {

    @Mock
    private BodegaRepository bodegaRepository;

    @InjectMocks
    private BodegaServiceImpl bodegaService;

    @Test
    void crearBodega_debeRetornarBodegaGuardada() {
        Bodega bodega = new Bodega();
        bodega.setCiudad("Cali");

        when(bodegaRepository.save(bodega)).thenReturn(bodega);

        Bodega resultado = bodegaService.crearBodega(bodega);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCiudad()).isEqualTo("Cali");
        verify(bodegaRepository, times(1)).save(bodega);
    }

    @Test
    void obtenerBodega_debeRetornarBodegaSiExiste() {
        Bodega bodega = new Bodega();
        bodega.setBodegaId("123");

        when(bodegaRepository.existsById("123")).thenReturn(true);
        when(bodegaRepository.findById("123")).thenReturn(Optional.of(bodega));

        Optional<Bodega> resultado = bodegaService.obtenerBodega("123");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getBodegaId()).isEqualTo("123");
    }

    @Test
    void obtenerBodega_debeLanzarExcepcionSiNoExiste() {
        when(bodegaRepository.existsById("999")).thenReturn(false);

        assertThatThrownBy(() -> bodegaService.obtenerBodega("999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe una bodega con ese ID");
    }

    @Test
    void actualizarBodega_debeRetornarBodegaActualizada() {
        Bodega bodega = new Bodega();
        bodega.setBodegaId("123");
        bodega.setCiudad("Medellín");

        when(bodegaRepository.existsById("123")).thenReturn(true);
        when(bodegaRepository.save(bodega)).thenReturn(bodega);

        Bodega resultado = bodegaService.actualizarBodega(bodega);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCiudad()).isEqualTo("Medellín");
        verify(bodegaRepository, times(1)).save(bodega);
    }

    @Test
    void actualizarBodega_debeLanzarExcepcionSiNoExiste() {
        Bodega bodega = new Bodega();
        bodega.setBodegaId("999");

        when(bodegaRepository.existsById("999")).thenReturn(false);

        assertThatThrownBy(() -> bodegaService.actualizarBodega(bodega))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe una bodega con ese ID");
    }

    @Test
    void eliminarBodega_debeEliminarSiExiste() {
        when(bodegaRepository.existsById("123")).thenReturn(true);

        bodegaService.eliminarBodega("123");

        verify(bodegaRepository, times(1)).deleteById("123");
    }

    @Test
    void eliminarBodega_debeLanzarExcepcionSiNoExiste() {
        when(bodegaRepository.existsById("999")).thenReturn(false);

        assertThatThrownBy(() -> bodegaService.eliminarBodega("999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No existe una bodega con ese ID");
    }

    @Test
    void listarBodegas_debeRetornarListaDeBodegas() {
        Bodega b1 = new Bodega();
        b1.setCiudad("Cali");

        Bodega b2 = new Bodega();
        b2.setCiudad("Bogotá");

        when(bodegaRepository.findAll()).thenReturn(Arrays.asList(b1, b2));

        List<Bodega> resultado = bodegaService.listarBodegas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting("ciudad").containsExactly("Cali", "Bogotá");

    }
/*
Resultados: El código del test está bien, no deberías tener errores si:

Tienes spring-boot-starter-test en tu pom.xml.

El test está en la carpeta correcta (src/test/java). 20/09/2025
 */

}

