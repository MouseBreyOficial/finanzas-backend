package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioUpdateRequest {
    private Long id;
    private String nombreCompleto;
    private String correoElectronico;
    private String hashContrasena;
    private String usuarioModificacion;
}
