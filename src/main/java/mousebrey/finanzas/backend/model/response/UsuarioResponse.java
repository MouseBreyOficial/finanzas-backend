package mousebrey.finanzas.backend.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {
    private Long id;
    private String nombreUsuario;
    private String nombreCompleto;
    private String correoElectronico;
    private Integer estadoRegistro;
}
