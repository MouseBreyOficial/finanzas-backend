package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;

public interface WebPushService {
    ResponseClient<Void> enviarPrueba(Long idUsuario);
}