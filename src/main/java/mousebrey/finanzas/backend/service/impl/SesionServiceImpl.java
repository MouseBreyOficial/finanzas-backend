package mousebrey.finanzas.backend.service.impl;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.model.AuthRequest;
import mousebrey.finanzas.backend.model.AuthResponse;
import mousebrey.finanzas.backend.model.RegistroDto;
import mousebrey.finanzas.backend.model.ResponseClient;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import mousebrey.finanzas.backend.security.JwtUtil;
import mousebrey.finanzas.backend.service.SesionService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SesionServiceImpl implements SesionService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepo;
    private final AuthenticationManager authenticationManager;

    public ResponseClient<AuthResponse> login(AuthRequest request) {
        try {
            Usuario usuario = usuarioRepo.findByNombreUsuarioIgnoreCase(request.getUsername())
                    .orElseThrow(() -> new ValidationException(Constant.CODIGO_ERROR, "Usuario o contraseña incorrectos"));

            if (usuario.getEstadoRegistro() == null || usuario.getEstadoRegistro() != 1) {
                throw new ValidationException(Constant.CODIGO_ERROR, "La cuenta se encuentra desactivada");
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            String token = jwtUtil.generarToken(usuario.getNombreUsuario());

            AuthResponse authResponse = new AuthResponse(
                    token,
                    "Bearer",
                    usuario.getNombreUsuario(),
                    usuario.getNombreCompleto()
            );

            return ResponseClient.setOk(authResponse);

        } catch (BadCredentialsException e) {
            throw new ValidationException(Constant.CODIGO_ERROR, "Usuario o contraseña incorrectos");
        } catch (AuthenticationException e) {
            throw new ValidationException(Constant.CODIGO_ERROR, "Error de autenticación");
        } catch (Exception ex) {
            throw new ValidationException(Constant.CODIGO_ERROR, ex.getMessage());
        }
    }

    public ResponseClient<AuthResponse> registrar(RegistroDto dto) {
        String nombreUsuario = dto.getUsername() == null ? "" : dto.getUsername().trim();
        if (nombreUsuario.isBlank()) {
            throw new ValidationException(Constant.CODIGO_ERROR, "El nombre de usuario es obligatorio");
        }
        if (usuarioRepo.existsNombreUsuarioEnTodaLaTabla(nombreUsuario)) {
            throw new ValidationException(Constant.CODIGO_ERROR, "El nombre de usuario ya se encuentra registrado");
        }

        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombreUsuario(nombreUsuario);
            nuevo.setHashContrasena(passwordEncoder.encode(dto.getPassword()));
            nuevo.setNombreCompleto(dto.getNombreCompleto());
            nuevo.setCorreoElectronico(dto.getEmail());
            nuevo.setUsuarioCreacion(dto.getUsuarioCreacion());
            nuevo.setEstadoRegistro(1);
            nuevo.setFechaCreacion(LocalDateTime.now());

            usuarioRepo.save(nuevo);

            String token = jwtUtil.generarToken(nuevo.getNombreUsuario());

            AuthResponse response = new AuthResponse(token, "Bearer", nuevo.getNombreUsuario(), nuevo.getNombreCompleto());

            return ResponseClient.setOk(response);
        } catch (Exception ex) {
            throw new ValidationException(Constant.CODIGO_ERROR, ex.getMessage());
        }

    }
}
