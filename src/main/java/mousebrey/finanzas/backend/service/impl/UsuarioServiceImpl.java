package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.model.ResponseClientList;
import mousebrey.finanzas.backend.model.request.UsuarioDeleteRequest;
import mousebrey.finanzas.backend.model.request.UsuarioRequest;
import mousebrey.finanzas.backend.model.request.UsuarioUpdateRequest;
import mousebrey.finanzas.backend.model.response.UsuarioResponse;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.service.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseClient<UsuarioResponse> registrar(UsuarioRequest request) {
        try {
            String nombreUsuario = request.getNombreUsuario() == null ? "" : request.getNombreUsuario().trim();

            if (nombreUsuario.isBlank()) {
                throw new ValidationException(Constant.CODIGO_ERROR, "El nombre de usuario es obligatorio");
            }

            if (usuarioRepository.existsNombreUsuarioEnTodaLaTabla(nombreUsuario)) {
                throw new ValidationException(Constant.CODIGO_ERROR, "El nombre de usuario ya se encuentra registrado");
            }

            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombreUsuario);

            if (request.getHashContrasena() == null || request.getHashContrasena().isBlank()) {
                throw new ValidationException(Constant.CODIGO_ERROR, "La contraseña es obligatoria");
            }

            usuario.setHashContrasena(passwordEncoder.encode(request.getHashContrasena()));
            usuario.setNombreCompleto(request.getNombreCompleto());
            usuario.setCorreoElectronico(request.getCorreoElectronico());
            usuario.setUsuarioCreacion(request.getUsuarioCreacion());
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setEstadoRegistro(1);

            usuarioRepository.save(usuario);

            return ResponseClient.setOk(mapToResponse(usuario));
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClient<UsuarioResponse> actualizar(UsuarioUpdateRequest request) {
        try {
            Usuario usuario = usuarioRepository.findById(request.getId())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));

            usuario.setNombreCompleto(request.getNombreCompleto());
            usuario.setCorreoElectronico(request.getCorreoElectronico());
            if (request.getHashContrasena() != null && !request.getHashContrasena().isBlank()) {
                usuario.setHashContrasena(passwordEncoder.encode(request.getHashContrasena()));
            }
            usuario.setUsuarioModificacion(request.getUsuarioModificacion());
            usuario.setFechaModificacion(LocalDateTime.now());

            usuarioRepository.save(usuario);

            return ResponseClient.setOk(mapToResponse(usuario));
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClient<Void> eliminar(UsuarioDeleteRequest request) {
        try {
            Usuario usuario = usuarioRepository.findById(request.getId())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));

            if ("administrador".equalsIgnoreCase(usuario.getNombreUsuario().trim())) {
                throw new ValidationException(Constant.CODIGO_ERROR, "El usuario Administrador no puede ser desactivado");
            }

            usuario.setEstadoRegistro(0);
            usuario.setUsuarioBaja(request.getUsuarioBaja());
            usuario.setDescripcionBaja(request.getDescripcionBaja());
            usuario.setFechaBaja(LocalDateTime.now());

            usuarioRepository.save(usuario);

            return ResponseClient.setOk(null);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ValidationException(Constant.CODIGO_ERROR, Constant.MENSAJE_ERROR);
        }
    }

    @Override
    public ResponseClient<Void> activar(Long id, String usuarioModificacion) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));
        usuario.setEstadoRegistro(1);
        usuario.setDescripcionBaja(null);
        usuario.setFechaBaja(null);
        usuario.setUsuarioBaja(null);
        usuario.setUsuarioModificacion(usuarioModificacion);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuarioRepository.save(usuario);
        return ResponseClient.setOk(null);
    }

    @Override
    public ResponseClientList<UsuarioResponse> listar() {
        List<UsuarioResponse> lista = usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseClientList.setOk(lista);
    }

    @Override
    public ResponseClient<UsuarioResponse> obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado"));
        return ResponseClient.setOk(mapToResponse(usuario));
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getIdUsuario());
        response.setNombreUsuario(usuario.getNombreUsuario());
        response.setNombreCompleto(usuario.getNombreCompleto());
        response.setCorreoElectronico(usuario.getCorreoElectronico());
        response.setEstadoRegistro(usuario.getEstadoRegistro());
        return response;
    }
}
