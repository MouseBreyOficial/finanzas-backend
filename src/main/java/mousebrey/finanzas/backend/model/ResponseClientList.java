package mousebrey.finanzas.backend.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import mousebrey.finanzas.backend.constante.Constant;

import java.util.List;

@Data
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseClientList<T> {

    @JsonProperty("codigo")
    private Integer codigo;

    @JsonProperty("mensaje")
    private String mensaje;

    @JsonProperty("data")
    private List<T> data;

    public static <T> ResponseClientList<T> setOk(List<T> data) {
        return ResponseClientList.<T>builder()
                .codigo(Constant.CODIGO_EXITO)
                .mensaje(Constant.MENSAJE_EXITO)
                .data(data)
                .build();
    }

    public static <T> ResponseClientList<T> setOk() {
        return ResponseClientList.<T>builder()
                .codigo(Constant.CODIGO_EXITO)
                .mensaje(Constant.MENSAJE_EXITO)
                .build();
    }

    public static <T> ResponseClientList<T> setEmpty(String mensaje) {
        return ResponseClientList.<T>builder()
                .codigo(Constant.CODIGO_EMPTY)
                .mensaje(mensaje)
                .build();
    }

    public static <T> ResponseClientList<T> setEmpty() {
        return ResponseClientList.<T>builder()
                .codigo(Constant.CODIGO_EMPTY)
                .mensaje(Constant.MENSAJE_EMPTY)
                .build();
    }

    public static <T> ResponseClientList<T> setError(String mensaje) {
        return ResponseClientList.<T>builder()
                .codigo(Constant.CODIGO_ERROR)
                .mensaje(mensaje)
                .build();
    }


}
