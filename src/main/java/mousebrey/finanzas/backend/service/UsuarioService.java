package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.UsuarioDeleteRequest;
import mousebrey.finanzas.backend.model.request.UsuarioRequest;
import mousebrey.finanzas.backend.model.request.UsuarioUpdateRequest;
import mousebrey.finanzas.backend.model.response.UsuarioResponse;

public interface UsuarioService {

    ResponseClient<UsuarioResponse> registrar(UsuarioRequest request);

    ResponseClient<UsuarioResponse> actualizar(UsuarioUpdateRequest request);

    ResponseClient<Void> eliminar(UsuarioDeleteRequest request);

    ResponseClient<Void> activar(Long id, String usuarioModificacion);

    ResponseClientList<UsuarioResponse> listar();

    ResponseClient<UsuarioResponse> obtenerPorId(Long id);
}
