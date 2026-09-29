package mousebrey.finanzas.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
public class AlertaResponse {
    private Long id;
    private String descripcion;
    private LocalDate fechaAlerta;
    private String tipo;
    private Boolean esRecurrente;
    private String frecuencia;
    private Integer diaMes;
    private String estado;
    private BigDecimal monto;
    private String categoria;
    private Long idCuenta;
    private Long idUsuario;
}

