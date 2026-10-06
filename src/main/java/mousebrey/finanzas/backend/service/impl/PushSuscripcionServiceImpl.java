package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.PushSuscripcion;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.request.PushSuscripcionRequest;
import mousebrey.finanzas.backend.repository.PushSuscripcionRepository;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.service.PushSuscripcionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PushSuscripcionServiceImpl implements PushSuscripcionService {

    private final PushSuscripcionRepository pushSuscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ResponseClient<Void> registrarSuscripcion(Long idUsuario, PushSuscripcionRequest request) {

        Usuario usuario = obtenerUsuario(idUsuario);

        PushSuscripcion suscripcion = pushSuscripcionRepository.findByEndpoint(
                request.getEndpoint()).orElseGet(PushSuscripcion::new);

        suscripcion.setUsuario(usuario);
        suscripcion.setEndpoint(request.getEndpoint());
        suscripcion.setP256dh(request.getP256dh());
        suscripcion.setAuth(request.getAuth());
        suscripcion.setEstadoRegistro(Constant.ESTADO_ACTIVO);

        pushSuscripcionRepository.save(suscripcion);

        return ResponseClient.setOk();
    }

    @Override
    @Transactional
    public ResponseClient<Void> eliminarSuscripcion(Long idUsuario, String endpoint) {

        PushSuscripcion suscripcion = pushSuscripcionRepository
                .findByEndpointAndUsuarioIdUsuario(endpoint, idUsuario)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_ERROR,
                        "La suscripción no existe para el usuario"));

        suscripcion.setEstadoRegistro(Constant.ESTADO_INACTIVO);
        pushSuscripcionRepository.save(suscripcion);

        return ResponseClient.setOk();
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseClient<Map<String, Boolean>> estado(Long idUsuario) {

        obtenerUsuario(idUsuario);
        boolean activo = pushSuscripcionRepository.existsByUsuarioIdUsuarioAndEstadoRegistro(
                        idUsuario, Constant.ESTADO_ACTIVO);

        return ResponseClient.setOk(Map.of("activo", activo));
    }

    private Usuario obtenerUsuario(Long idUsuario) {

        return usuarioRepository
                .findById(idUsuario)
                .orElseThrow(() -> new ValidationException(
                        Constant.CODIGO_ERROR, "Usuario no encontrado"));
    }
}