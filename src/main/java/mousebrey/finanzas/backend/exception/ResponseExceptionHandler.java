package mousebrey.finanzas.backend.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mousebrey.finanzas.backend.model.ResponseClient;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@RestController
@Slf4j
@RequiredArgsConstructor
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> messageSource.getMessage(error, LocaleContextHolder.getLocale()))
                .distinct()
                .collect(Collectors.joining("; "));

        log.error("Validación de atributos - ResponseExceptionHandler ERROR: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseClient.setError(errors));
    }

    @ExceptionHandler(ValidationHttpException.class)
    public final ResponseEntity<ResponseClient<Void>> validationHttpException(ValidationHttpException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ResponseClient.<Void>builder()
                        .codigo(ex.getCodigo())
                        .mensaje(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<ResponseClient<Void>> validationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseClient.<Void>builder()
                        .codigo(ex.getCodigo())
                        .mensaje(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(InternalErrorExceptionHandler.class)
    public final ResponseEntity<ResponseClient<Void>> internalErrorExceptionHandler(InternalErrorExceptionHandler ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseClient.setError(ex.getMessage()));
    }
}
