package mousebrey.finanzas.backend.repository;

import mousebrey.finanzas.backend.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByUsuarioIdUsuario(Long idUsuario);
    List<Alerta> findByEstadoAndFechaAlerta(String estado, LocalDate fechaAlerta);
    List<Alerta> findByEstadoAndFechaAlertaLessThanEqual(String estado, LocalDate fechaAlerta);
}