package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.IngresoRequest;
import mousebrey.finanzas.backend.model.request.IngresoUpdateRequest;
import mousebrey.finanzas.backend.model.response.IngresoResponse;

public interface IngresoService {
    ResponseClient<IngresoResponse> registrar(IngresoRequest request);
    ResponseClient<IngresoResponse> actualizar(IngresoUpdateRequest request);
    ResponseClientList<IngresoResponse> listarPorCuenta(Long idCuenta);
    ResponseClient<IngresoResponse> obtenerPorId(Long id);
}

