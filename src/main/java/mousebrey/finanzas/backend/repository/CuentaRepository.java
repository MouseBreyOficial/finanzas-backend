package mousebrey.finanzas.backend.repository;

import mousebrey.finanzas.backend.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByUsuarioIdUsuario(Long idUsuario);
}
