package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransferenciaRequest {
    private Long idUsuario;
    private Long idCuentaOrigen;
    private Long idCuentaDestino;
    private BigDecimal monto;
    private LocalDate fechaTransferencia;
    private String descripcion;
    private String usuarioCreacion;
}
