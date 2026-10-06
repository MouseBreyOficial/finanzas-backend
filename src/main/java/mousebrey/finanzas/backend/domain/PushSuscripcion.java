package mousebrey.finanzas.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "push_suscripciones",
        uniqueConstraints = {
                @UniqueConstraint(
                name = "uk_push_suscripciones_endpoint",
                columnNames = "endpoint")
        }
)
public class PushSuscripcion extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "push_suscripciones_id_seq")
    @SequenceGenerator(name = "push_suscripciones_id_seq", sequenceName = "push_suscripciones_id_seq", allocationSize = 1)
    @Column(name = "id_push_suscripcion")
    private Long idPushSuscripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false, foreignKey = @ForeignKey(name = "fk_push_suscripciones_usuario"))
    private Usuario usuario;

    @Column(name = "endpoint", nullable = false, columnDefinition = "TEXT")
    private String endpoint;

    @Column(name = "p256dh", nullable = false, columnDefinition = "TEXT")
    private String p256dh;

    @Column(name = "auth", nullable = false, columnDefinition = "TEXT")
    private String auth;

    @Column(name = "estado_registro")
    private Integer estadoRegistro = 1;
}
