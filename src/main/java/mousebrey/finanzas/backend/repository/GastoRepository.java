package mousebrey.finanzas.backend.repository;

import mousebrey.finanzas.backend.domain.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;

@Repository
public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByCuentaIdCuenta(Long idCuenta);

    @Query("SELECT g.categoria, AVG(g.monto) FROM Gasto g WHERE g.cuenta.usuario.idUsuario = :idUsuario GROUP BY g.categoria")
    List<Object[]> promedioPorCategoria(@Param("idUsuario") Long idUsuario);

    @Query("SELECT g FROM Gasto g WHERE g.cuenta.usuario.idUsuario = :idUsuario AND g.fecha BETWEEN :desde AND :hasta ORDER BY g.fecha DESC")
    List<Gasto> findResumen(@Param("idUsuario") Long idUsuario, @Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);

    @Query("SELECT DISTINCT LOWER(TRIM(g.categoria)) FROM Gasto g WHERE g.cuenta.usuario.idUsuario = :idUsuario AND g.categoria IS NOT NULL ORDER BY LOWER(TRIM(g.categoria))")
    List<String> categoriasPorUsuario(@Param("idUsuario") Long idUsuario);

}
