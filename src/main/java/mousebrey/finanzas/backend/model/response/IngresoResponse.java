package mousebrey.finanzas.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class IngresoResponse {
    private Long id;
    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;
    private Long idCuenta;
}
