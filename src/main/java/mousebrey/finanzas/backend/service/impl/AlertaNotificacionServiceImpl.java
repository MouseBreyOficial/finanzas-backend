package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mousebrey.finanzas.backend.domain.Alerta;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.repository.AlertaRepository;
import mousebrey.finanzas.backend.service.AlertaNotificacionService;
import mousebrey.finanzas.backend.service.WebPushService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertaNotificacionServiceImpl implements AlertaNotificacionService {

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String TIPO_PROXIMO_VENCIMIENTO = "PROXIMO_VENCIMIENTO";
    private static final String TIPO_VENCE_HOY = "VENCE_HOY";
    private static final String TIPO_VENCIDO = "VENCIDO";

    private final AlertaRepository alertaRepository;
    private final WebPushService webPushService;

    @Value("${alertas.notificaciones.dias-anticipacion:3}")
    private int diasAnticipacion;

    /*
     * ============================================================
     * PROCESAR NOTIFICACIONES
     * ============================================================
     *
     * Se procesan:
     *
     * - Alertas que vencen dentro de los próximos N días.
     * - Alertas que vencen hoy.
     * - Alertas que ya vencieron y continúan PENDIENTES.
     *
     * La protección mediante fechaUltimaNotificacion evita
     * duplicados dentro del mismo turno.
     *
     * Turnos actuales:
     * - Mañana: antes de las 14:00
     * - Tarde/noche: desde las 14:00
     *
     * El scheduler es quien determina las horas reales de ejecución,
     * por defecto 08:00 y 20:00.
     * ============================================================
     */
    @Override
    @Transactional
    public ResponseClient<Void> procesarNotificaciones() {

        LocalDate hoy = LocalDate.now();
        LocalDate fechaLimite = hoy.plusDays(diasAnticipacion);

        List<Alerta> alertas = alertaRepository.findByEstadoAndFechaAlertaLessThanEqual(ESTADO_PENDIENTE, fechaLimite);

        log.info("Procesando notificaciones. Fecha actual: {}, fecha límite: {}, alertas encontradas: {}",
                hoy, fechaLimite, alertas.size());

        for (Alerta alerta : alertas) {
            procesarAlerta(alerta, hoy);
        }

        return ResponseClient.setOk();
    }

    /*
     * ============================================================
     * PROCESAR UNA ALERTA
     * ============================================================
     */
    private void procesarAlerta(Alerta alerta, LocalDate hoy) {
        LocalDateTime ahora = LocalDateTime.now();

        if (yaFueNotificadaEnEstaEjecucion(alerta, ahora)) {
            log.info("La alerta {} ya fue notificada en este turno.", alerta.getIdAlerta());
            return;
        }

        LocalDate fechaVencimiento = alerta.getFechaAlerta();

        /*
         * Calculamos cuántos días faltan.
         *
         * Ejemplos:
         *
         * hoy 06/10 - vence 09/10 = 3
         * hoy 06/10 - vence 08/10 = 2
         * hoy 06/10 - vence 07/10 = 1
         * hoy 06/10 - vence 06/10 = 0
         * hoy 06/10 - venció 05/10 = -1
         */
        long diasParaVencimiento = ChronoUnit.DAYS.between(hoy, fechaVencimiento);

        if (diasParaVencimiento > 0) {
            procesarProximoVencimiento(alerta, diasParaVencimiento);
            return;
        }

        if (diasParaVencimiento == 0) {
            procesarVenceHoy(alerta);
            return;
        }

        procesarVencida(alerta, Math.abs(diasParaVencimiento));
    }

    /*
     * ============================================================
     * PRÓXIMO VENCIMIENTO
     * ============================================================
     */
    private void procesarProximoVencimiento(Alerta alerta, long dias) {

        String mensaje = construirMensajeProximoVencimiento(alerta, dias);

        boolean enviado = webPushService.enviarNotificacion(
                alerta.getUsuario().getIdUsuario(),
                "Próximo vencimiento",
                mensaje,
                "/alertas"
        );

        if (enviado) {
            registrarNotificacion(alerta, TIPO_PROXIMO_VENCIMIENTO);
        }
    }

    /*
     * ============================================================
     * VENCE HOY
     * ============================================================
     */
    private void procesarVenceHoy(Alerta alerta) {

        String mensaje = construirMensajeVenceHoy(alerta);

        boolean enviado = webPushService.enviarNotificacion(
                alerta.getUsuario().getIdUsuario(),
                "Pago vence hoy",
                mensaje,
                "/alertas"
        );

        if (enviado) {
            registrarNotificacion(alerta, TIPO_VENCE_HOY);
        }
    }

    /*
     * ============================================================
     * ALERTA VENCIDA
     * ============================================================
     */
    private void procesarVencida(Alerta alerta, long diasVencida) {

        String mensaje = construirMensajeVencido(alerta, diasVencida);

        boolean enviado = webPushService.enviarNotificacion(
                alerta.getUsuario().getIdUsuario(),
                "Pago vencido",
                mensaje,
                "/alertas"
        );

        if (enviado) {
            registrarNotificacion(alerta, TIPO_VENCIDO);
        }
    }

    /*
     * ============================================================
     * VALIDAR SI YA FUE NOTIFICADA EN EL TURNO ACTUAL
     * ============================================================
     */
    private boolean yaFueNotificadaEnEstaEjecucion(
            Alerta alerta,
            LocalDateTime ahora
    ) {

        LocalDateTime ultimaNotificacion =
                alerta.getFechaUltimaNotificacion();

        if (ultimaNotificacion == null) {
            return false;
        }

        /*
         * Si la última notificación corresponde a otro día,
         * se puede notificar normalmente.
         */
        if (!ultimaNotificacion.toLocalDate()
                .equals(ahora.toLocalDate())) {

            return false;
        }

        /*
         * Turno de mañana:
         *
         * Si estamos antes de las 14:00 y ya se notificó
         * durante la mañana, no volver a enviar.
         */
        if (ahora.getHour() < 14) {

            return ultimaNotificacion.getHour() < 14;
        }

        /*
         * Turno de noche:
         *
         * Si estamos después de las 14:00 y ya se notificó
         * durante este turno, no volver a enviar.
         */
        return ultimaNotificacion.getHour() >= 14;
    }

    /*
     * ============================================================
     * REGISTRAR NOTIFICACIÓN
     * ============================================================
     */
    private void registrarNotificacion(Alerta alerta, String tipo) {

        alerta.setFechaUltimaNotificacion(LocalDateTime.now());
        alerta.setTipoUltimaNotificacion(tipo);

        alertaRepository.save(alerta);

        log.info("Alerta {} notificada. Tipo: {}", alerta.getIdAlerta(), tipo);
    }

    /*
     * ============================================================
     * MENSAJE - PRÓXIMO VENCIMIENTO
     * ============================================================
     */
    private String construirMensajeProximoVencimiento(Alerta alerta, long dias) {

        String textoDias;

        if (dias == 1) {
            textoDias = "vence mañana";
        } else {
            textoDias = "vence en " + dias + " días";
        }

        String mensaje = alerta.getDescripcion() + " " + textoDias;

        if (alerta.getMonto() != null) {
            mensaje += " - S/ " + alerta.getMonto();
        }

        return mensaje;
    }

    /*
     * ============================================================
     * MENSAJE - VENCE HOY
     * ============================================================
     */
    private String construirMensajeVenceHoy(Alerta alerta) {
        String mensaje = alerta.getDescripcion() + " vence hoy";

        if (alerta.getMonto() != null) {
            mensaje += " - S/ " + alerta.getMonto();
        }

        return mensaje;
    }

    /*
     * ============================================================
     * MENSAJE - VENCIDO
     * ============================================================
     */
    private String construirMensajeVencido(Alerta alerta, long diasVencida) {
        String textoDias;

        if (diasVencida == 1) {
            textoDias = "venció ayer";
        } else {
            textoDias = "venció hace " + diasVencida + " días";
        }

        String mensaje = alerta.getDescripcion() + " " + textoDias;

        if (alerta.getMonto() != null) {
            mensaje += " - S/ " + alerta.getMonto();
        }

        return mensaje;
    }

}