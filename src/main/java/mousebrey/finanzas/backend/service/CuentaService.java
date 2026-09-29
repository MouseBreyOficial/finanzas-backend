package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.CuentaRequest;
import mousebrey.finanzas.backend.model.request.CuentaUpdateRequest;
import mousebrey.finanzas.backend.model.response.CuentaResponse;

public interface CuentaService {
    ResponseClient<CuentaResponse> registrar(CuentaRequest request);
    ResponseClient<CuentaResponse> actualizar(CuentaUpdateRequest request);
    ResponseClientList<CuentaResponse> listarPorUsuario(Long idUsuario);
    ResponseClient<CuentaResponse> obtenerPorId(Long id);
}
