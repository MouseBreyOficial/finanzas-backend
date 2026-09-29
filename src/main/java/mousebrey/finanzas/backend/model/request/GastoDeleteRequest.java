package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GastoDeleteRequest {
    private Long id;
    private String usuarioBaja;
    private String descripcionBaja;
}
