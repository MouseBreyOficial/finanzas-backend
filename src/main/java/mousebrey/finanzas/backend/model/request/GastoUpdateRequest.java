package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class GastoUpdateRequest {
    private Long id;
    private BigDecimal monto;
    private LocalDate fecha;
    private String categoria;
    private String descripcion;
    private String usuarioModificacion;
}

