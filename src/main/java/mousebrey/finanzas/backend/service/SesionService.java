package mousebrey.finanzas.backend.service;

import mousebrey.finanzas.backend.model.AuthRequest;
import mousebrey.finanzas.backend.model.AuthResponse;
import mousebrey.finanzas.backend.model.RegistroDto;
import mousebrey.finanzas.backend.model.ResponseClient;

public interface SesionService {
    ResponseClient<AuthResponse> login(AuthRequest request);
    ResponseClient<AuthResponse> registrar(RegistroDto dto);
}
