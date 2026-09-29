package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.AlertaRequest;
import mousebrey.finanzas.backend.model.request.AlertaUpdateRequest;
import mousebrey.finanzas.backend.model.response.AlertaResponse;

public interface AlertaService {
    ResponseClient<AlertaResponse> registrar(AlertaRequest request);
    ResponseClient<AlertaResponse> actualizar(AlertaUpdateRequest request);
    ResponseClientList<AlertaResponse> listarPorUsuario(Long idUsuario);
    ResponseClient<AlertaResponse> obtenerPorId(Long id);
    ResponseClient<AlertaResponse> marcarPagada(Long id, String usuario);
}

