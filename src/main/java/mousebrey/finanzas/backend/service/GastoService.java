package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.GastoRequest;
import mousebrey.finanzas.backend.model.request.GastoUpdateRequest;
import mousebrey.finanzas.backend.model.response.GastoResponse;

import java.util.Map;
import java.time.LocalDate;

public interface GastoService {
    ResponseClient<GastoResponse> registrar(GastoRequest request);
    ResponseClient<GastoResponse> actualizar(GastoUpdateRequest request);
    ResponseClientList<GastoResponse> listarPorCuenta(Long idCuenta);
    ResponseClient<GastoResponse> obtenerPorId(Long id);
    ResponseClient<Map<String, Double>> promedioPorCategoria(Long idUsuario);
    ResponseClient<Map<String, Object>> resumen(Long idUsuario, LocalDate desde, LocalDate hasta);
    ResponseClientList<String> categorias(Long idUsuario);
}
