package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IngresoRequest {
    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;
    private Long idCuenta;
    private String usuarioCreacion;
}
