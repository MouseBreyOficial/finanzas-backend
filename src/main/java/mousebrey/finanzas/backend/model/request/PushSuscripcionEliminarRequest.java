package mousebrey.finanzas.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushSuscripcionEliminarRequest {

    @NotBlank(message = "El endpoint es obligatorio")
    private String endpoint;
}