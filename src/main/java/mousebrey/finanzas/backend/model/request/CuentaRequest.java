package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaRequest {
    private String nombreCuenta;
    private BigDecimal saldoActual;
    private BigDecimal saldoInicial;
    private Long idUsuario;
    private String usuarioCreacion;
}

