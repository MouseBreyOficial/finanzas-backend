package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.request.PushSuscripcionRequest;

import java.util.Map;

public interface PushSuscripcionService {

    ResponseClient<Void> registrarSuscripcion(Long idUsuario, PushSuscripcionRequest request);
    ResponseClient<Void> eliminarSuscripcion(Long idUsuario, String endpoint);
    ResponseClient<Map<String, Boolean>> estado(Long idUsuario);
}