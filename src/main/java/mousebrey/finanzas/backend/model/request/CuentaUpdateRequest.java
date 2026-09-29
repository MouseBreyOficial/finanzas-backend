package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CuentaUpdateRequest {
    private Long id;
    private String nombreCuenta;
    private BigDecimal saldoActual;
    private String usuarioModificacion;
}
