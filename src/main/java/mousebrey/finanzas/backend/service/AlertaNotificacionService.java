package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;

public interface AlertaNotificacionService {
    ResponseClient<Void> procesarNotificaciones();
}