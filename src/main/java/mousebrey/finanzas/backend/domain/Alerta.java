package mousebrey.finanzas.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "alertas")
@Getter
@Setter
public class Alerta extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alertas_id_seq")
    @SequenceGenerator(name = "alertas_id_seq", sequenceName = "alertas_id_seq", allocationSize = 1)
    @Column(name = "id_alerta")
    private Long idAlerta;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "fecha_alerta", nullable = false)
    private LocalDate fechaAlerta;

    @Column(nullable = false, length = 50)
    private String tipo;

    @Column(name = "es_recurrente")
    private Boolean esRecurrente = false;

    private String frecuencia;

    @Column(name = "dia_mes")
    private Integer diaMes;

    private String estado = "PENDIENTE";

    private BigDecimal monto;

    private String categoria;

    @ManyToOne
    @JoinColumn(name = "id_cuenta")
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
