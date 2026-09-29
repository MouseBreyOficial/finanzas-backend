package mousebrey.finanzas.backend.security;

import lombok.RequiredArgsConstructor;
import mousebrey.finanzas.backend.constante.Constant;
import mousebrey.finanzas.backend.domain.Usuario;
import mousebrey.finanzas.backend.exception.ValidationException;
import mousebrey.finanzas.backend.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByNombreUsuarioIgnoreCase(username)
                .orElseThrow(() -> new ValidationException(Constant.CODIGO_EMPTY, "Usuario no encontrado: " + username));

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getHashContrasena())
                .disabled(usuario.getEstadoRegistro() == null || usuario.getEstadoRegistro() != 1)
                .build();
    }
}
