package mousebrey.finanzas.backend.exception;

public class ValidationException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private final Integer codigo;

	public ValidationException(Integer codigo, String mensaje) {
		super(mensaje);
		this.codigo = codigo;
	}

	public Integer getCodigo() {
		return codigo;
	}
}