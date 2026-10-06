package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.PushSuscripcion;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.repository.PushSuscripcionRepository;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.service.WebPushService;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebPushServiceImpl implements WebPushService {

    private final PushSuscripcionRepository pushSuscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    @Value("${webpush.vapid.public-key}")
    private String vapidPublicKey;

    @Value("${webpush.vapid.private-key}")
    private String vapidPrivateKey;

    @Value("${webpush.vapid.subject}")
    private String vapidSubject;

    @Override
    public ResponseClient<Void> enviarPrueba(Long idUsuario) {

        validarUsuario(idUsuario);

        boolean enviado = enviarNotificacion(idUsuario, "Finanzas Personales", "¡Web Push funciona correctamente!", "/");

        if (!enviado) {
            throw new ValidationException(Constant.CODIGO_ERROR, "No se pudo enviar la notificación a ningún dispositivo");
        }

        return ResponseClient.setOk();
    }

    @Override
    public boolean enviarNotificacion(Long idUsuario, String titulo, String mensaje, String url) {

        List<PushSuscripcion> suscripciones = pushSuscripcionRepository.
                findByUsuarioIdUsuarioAndEstadoRegistro(idUsuario, Constant.ESTADO_ACTIVO);

        /*
         * Es totalmente válido que un usuario todavía
         * no haya activado las notificaciones Push.
         */
        if (suscripciones.isEmpty()) {
            log.info("Usuario {} sin dispositivos Push activos", idUsuario);
            return false;
        }

        validarConfiguracionVapid();

        PushService pushService = crearPushService();

        String payload = crearPayload(titulo, mensaje, url);

        int enviados = 0;

        for (PushSuscripcion suscripcion : suscripciones) {

            try {

                Notification notification = new Notification(
                        suscripcion.getEndpoint(),
                        suscripcion.getP256dh(),
                        suscripcion.getAuth(),
                        payload.getBytes(StandardCharsets.UTF_8)
                );

                pushService.send(notification);

                enviados++;

                log.info("Web Push enviado correctamente. Usuario: {}", idUsuario);

            } catch (Exception e) {
                /*
                 * El fallo de un dispositivo no impide
                 * intentar enviar a los demás.
                 */
                log.error("Error enviando Web Push al usuario {}: {}", idUsuario, e.getMessage(), e);
            }
        }

        return enviados > 0;
    }

    private String crearPayload(String titulo, String mensaje, String url
    ) {
        /*
         * Usamos String.formatted() para poder reutilizar
         * el mismo WebPushService con cualquier alerta.
         */
        return """
                {
                  "notification": {
                    "title": "%s",
                    "body": "%s",
                    "icon": "/icons/icon-192x192.png",
                    "badge": "/icons/icon-96x96.png",
                    "data": {
                      "url": "%s"
                    }
                  }
                }
                """.formatted(
                escaparJson(titulo),
                escaparJson(mensaje),
                escaparJson(url)
        );
    }

    private String escaparJson(String valor) {

        if (valor == null) {
            return "";
        }

        return valor
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private PushService crearPushService() {
        try {
            if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(new BouncyCastleProvider());
            }

            PushService pushService = new PushService();
            pushService.setPublicKey(vapidPublicKey);
            pushService.setPrivateKey(vapidPrivateKey);
            pushService.setSubject(vapidSubject);
            return pushService;

        } catch (Exception e) {
            log.error("Error inicializando Web Push: {}", e.getMessage(), e);
            throw new ValidationException(Constant.CODIGO_ERROR, "No se pudo inicializar el servicio de notificaciones");
        }
    }

    private void validarUsuario(Long idUsuario) {
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new ValidationException(Constant.CODIGO_ERROR, "Usuario no encontrado");
        }
    }

    private void validarConfiguracionVapid() {

        if (vapidPublicKey == null
                || vapidPublicKey.isBlank()
                || vapidPrivateKey == null
                || vapidPrivateKey.isBlank()
                || vapidSubject == null
                || vapidSubject.isBlank()) {

            throw new ValidationException(Constant.CODIGO_ERROR, "La configuración VAPID no está completa");
        }
    }
}