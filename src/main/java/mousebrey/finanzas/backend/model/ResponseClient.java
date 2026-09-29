package mousebrey.finanzas.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import mousebrey.finanzas.backend.constante.Constant;

@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseClient<T> {

    @JsonProperty("codigo")
    private Integer codigo;

    @JsonProperty("mensaje")
    private String mensaje;

    @JsonProperty("data")
    private T data;

    public static <T> ResponseClient<T> setOk(T data) {
        return ResponseClient.<T>builder()
                .codigo(Constant.CODIGO_EXITO)
                .mensaje(Constant.MENSAJE_EXITO)
                .data(data)
                .build();
    }

    public static <T> ResponseClient<T> setOk() {
        return ResponseClient.<T>builder()
                .codigo(Constant.CODIGO_EXITO)
                .mensaje(Constant.MENSAJE_EXITO)
                .build();
    }

    public static <T> ResponseClient<T> setEmpty(String mensaje) {
        return ResponseClient.<T>builder()
                .codigo(Constant.CODIGO_EMPTY)
                .mensaje(mensaje)
                .build();
    }

    public static <T> ResponseClient<T> setEmpty() {
        return ResponseClient.<T>builder()
                .codigo(Constant.CODIGO_EMPTY)
                .mensaje(Constant.MENSAJE_EMPTY)
                .build();
    }

    public static <T> ResponseClient<T> setError(String mensaje) {
        return ResponseClient.<T>builder()
                .codigo(Constant.CODIGO_ERROR)
                .mensaje(mensaje)
                .build();
    }

}
