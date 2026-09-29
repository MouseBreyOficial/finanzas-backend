package mousebrey.finanzas.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
public class Cuenta extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cuentas_id_seq")
    @SequenceGenerator(name = "cuentas_id_seq", sequenceName = "cuentas_id_seq", allocationSize = 1)
    @Column(name = "id_cuenta")
    private Long idCuenta;

    @Column(name = "nombre_cuenta", nullable = false, length = 100)
    private String nombreCuenta;

    @Column(name = "saldo_inicial", precision = 12, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_actual", precision = 12, scale = 2)
    private BigDecimal saldoActual;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
