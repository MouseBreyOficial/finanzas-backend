package mousebrey.finanzas.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaResponse {
    private Long id;
    private String nombreCuenta;
    private BigDecimal saldoActual;
    private BigDecimal saldoInicial;
    private Long idUsuario;
}

