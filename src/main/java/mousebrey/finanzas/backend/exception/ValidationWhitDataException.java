package mousebrey.finanzas.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ValidationWhitDataException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer codigo;
    private Object data;
    private HttpStatus status;

    public ValidationWhitDataException(Integer codigo, String mensaje, Object data) {
        super(mensaje);
        this.codigo = codigo;
        this.data = data;
    }

}
