package mousebrey.finanzas.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "gastos")
public class Gasto extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gastos_id_seq")
    @SequenceGenerator(name = "gastos_id_seq", sequenceName = "gastos_id_seq", allocationSize = 1)
    @Column(name = "id_gasto")
    private Long idGasto;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;
}
