package mousebrey.finanzas.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferenciaResponse {
    private Long id;
    private Long idUsuario;
    private Long idCuentaOrigen;
    private String nombreCuentaOrigen;
    private Long idCuentaDestino;
    private String nombreCuentaDestino;
    private BigDecimal monto;
    private LocalDate fechaTransferencia;
    private String descripcion;
}
