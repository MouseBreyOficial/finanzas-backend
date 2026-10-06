package mousebrey.finanzas.backend.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mousebrey.finanzas.backend.service.AlertaNotificacionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertaNotificacionScheduler {

    private final AlertaNotificacionService alertaNotificacionService;

    /*
     * ============================================================
     * PROCESAR NOTIFICACIONES DE ALERTAS
     * ============================================================
     *
     * Por defecto se ejecuta todos los días:
     *
     * 08:00 AM
     * 08:00 PM
     *
     * Zona horaria: Perú.
     *
     * El cron puede modificarse mediante la variable:
     *
     * ALERTAS_NOTIFICACIONES_CRON
     * ============================================================
     */
    @Scheduled(cron = "${alertas.notificaciones.cron}", zone = "America/Lima")
    public void procesarNotificaciones() {

        log.info("Iniciando procesamiento automático de notificaciones de alertas...");

        try {
            alertaNotificacionService.procesarNotificaciones();
            log.info("Procesamiento automático de notificaciones finalizado.");
        } catch (Exception e) {
            log.error("Error procesando automáticamente las notificaciones de alertas", e);
        }
    }
}