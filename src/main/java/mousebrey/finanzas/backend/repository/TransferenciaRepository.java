package mousebrey.finanzas.backend.repository;

import mousebrey.finanzas.backend.domain.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByUsuarioIdUsuarioOrderByFechaTransferenciaDescIdTransferenciaDesc(Long idUsuario);
}
