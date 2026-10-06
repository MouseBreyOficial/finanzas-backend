package mousebrey.finanzas.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mousebrey.finanzas.backend.domain.PushSuscripcion;
import java.util.Optional;
import java.util.List;

public interface PushSuscripcionRepository extends JpaRepository<PushSuscripcion, Long> {
    Optional<PushSuscripcion> findByEndpoint(String endpoint);
    Optional<PushSuscripcion>
    findByEndpointAndUsuarioIdUsuario(String endpoint, Long idUsuario);
    List<PushSuscripcion>
    findByUsuarioIdUsuarioAndEstadoRegistro(Long idUsuario, Integer estadoRegistro);
    boolean existsByUsuarioIdUsuarioAndEstadoRegistro(Long idUsuario, Integer estadoRegistro);
}