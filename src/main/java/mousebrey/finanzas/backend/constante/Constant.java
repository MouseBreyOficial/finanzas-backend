package mousebrey.finanzas.backend.constante;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constant {
    public static final Integer CODIGO_EXITO = 0;
    public static final Integer CODIGO_EMPTY = 1;
    public static final Integer CODIGO_ERROR = -1;
    public static final String MENSAJE_EXITO = "OK";
    public static final String MENSAJE_EMPTY = "NO HAY DATA PARA ESTA CONSULTA";
    public static final String MENSAJE_ERROR = "ERROR EN NUESTRO SISTEMA";
    public static final Integer ESTADO_ACTIVO = 1;
    public static final Integer ESTADO_INACTIVO = 0;
}

