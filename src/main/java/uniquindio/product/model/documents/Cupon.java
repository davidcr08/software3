package uniquindio.product.model.documents;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.enums.EstadoCupon;
import uniquindio.product.enums.TipoCupon;

/**
 * Entidad JPA que representa un cupón de descuento.
 * Usa Lombok para generar getters y setters, y se mapea a la tabla "cupon".
 */
@Getter
@Setter
@Entity
@Table(name = "cupon")
public class Cupon {

    /**
     * Identificador único del cupón.
     * Se genera automáticamente utilizando UUID y se persiste en la columna "id".
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", length = 36)
    private String id;

    /**
     * Código único del cupón para su uso.
     */
    @Column(name = "codigo", unique = true, nullable = false)
    private String codigo;

    /**
     * Cédula del cliente al que pertenece el cupón.
     */
    @Column(name = "cedula_cliente")
    private String cedulaCliente;

    /**
     * Nombre descriptivo del cupón.
     */
    @Column(name = "nombre")
    private String nombre;

    /**
     * Porcentaje de descuento que aplica el cupón.
     */
    @Column(name = "porcentaje_descuento")
    private Float porcentajeDescuento;

    /**
     * Fecha de vencimiento del cupón.
     */
    @Column(name = "fecha_vencimiento")
    private Date fechaVencimiento;

    /**
     * Fecha de apertura/creación del cupón.
     */
    @Column(name = "fecha_apertura")
    private Date fechaApertura;

    /**
     * Tipo de cupón (público o único).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cupon")
    private TipoCupon tipoCupon;

    /**
     * Estado actual del cupón (disponible o no disponible).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCupon estado;
}
