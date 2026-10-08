package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.TransferenciaRequest;
import mousebrey.finanzas.backend.model.response.TransferenciaResponse;

public interface TransferenciaService {
    ResponseClient<TransferenciaResponse> registrar(TransferenciaRequest request);
    ResponseClientList<TransferenciaResponse> listarPorUsuario(Long idUsuario);
    ResponseClient<TransferenciaResponse> obtenerPorId(Long id);
}
