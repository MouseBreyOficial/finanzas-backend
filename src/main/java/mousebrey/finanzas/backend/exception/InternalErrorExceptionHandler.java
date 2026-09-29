package mousebrey.finanzas.backend.exception;

public class InternalErrorExceptionHandler extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InternalErrorExceptionHandler(String mensaje) {
        super(mensaje);
    }

}
