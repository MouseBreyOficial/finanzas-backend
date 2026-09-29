package mousebrey.finanzas.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationHttpException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer codigo;
    private HttpStatus status;

    public ValidationHttpException(HttpStatus status, Integer codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
        this.status = status;
    }

}
