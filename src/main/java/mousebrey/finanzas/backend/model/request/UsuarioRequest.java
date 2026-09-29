package mousebrey.finanzas.backend.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {
    private String nombreUsuario;
    private String hashContrasena;
    private String nombreCompleto;
    private String correoElectronico;
    private String usuarioCreacion;
}
