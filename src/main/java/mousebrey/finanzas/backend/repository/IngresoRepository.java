package mousebrey.finanzas.backend.repository;

import mousebrey.finanzas.backend.domain.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Long> {
    List<Ingreso> findByCuentaIdCuenta(Long idCuenta);
}

