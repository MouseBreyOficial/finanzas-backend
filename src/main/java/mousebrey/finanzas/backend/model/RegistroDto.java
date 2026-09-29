package mousebrey.finanzas.backend.model;

import lombok.Data;

@Data
public class RegistroDto {
    private String username;
    private String password;
    private String nombreCompleto;
    private String email;
    private String usuarioCreacion;
}
