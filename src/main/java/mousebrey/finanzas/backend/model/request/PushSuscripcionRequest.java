package mousebrey.finanzas.backend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushSuscripcionRequest {

    @NotBlank(message = "El endpoint es obligatorio")
    private String endpoint;

    @NotBlank(message = "La clave p256dh es obligatoria")
    private String p256dh;

    @NotBlank(message = "La clave auth es obligatoria")
    private String auth;
}
