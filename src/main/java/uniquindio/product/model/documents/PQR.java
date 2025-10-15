package uniquindio.product.model.documents;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import uniquindio.product.model.enums.CategoriaPqr;
import uniquindio.product.model.enums.EstadoPqr;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pqr")
public class PQR {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idPqr")
    private String idPqr;

    @Column(name = "idUsuario", nullable = false)
    private String idUsuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)

    private CategoriaPqr categoria;

    /*
     * SOLO PERMITE UN MAXIMO DE 3000 CARACTERES
     */
    @Column(name = "descripcion", nullable = false, length = 3000)
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;

    @Column(name = "idWorker")
    private String idWorker;

    @Column(name = "fechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fechaRespuesta")
    private LocalDateTime fechaRespuesta;

    @Enumerated(EnumType.STRING)

    @Column(name = "estadoPqr", nullable = false)
    private EstadoPqr estadoPqr;

    public void prePersist() {
        if (fechaCreacion == null) {
            fechaCreacion = LocalDateTime.now();
        }
    }
}
