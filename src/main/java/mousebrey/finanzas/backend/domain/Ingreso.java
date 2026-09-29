package mousebrey.finanzas.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ingresos")
@Getter
@Setter
public class Ingreso extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingresos_id_seq")
    @SequenceGenerator(name = "ingresos_id_seq", sequenceName = "ingresos_id_seq", allocationSize = 1)
    @Column(name = "id_ingreso")
    private Long idIngreso;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 255)
    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_cuenta", nullable = false)
    private Cuenta cuenta;
}
