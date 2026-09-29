package mousebrey.finanzas.backend.repository;

import mousebrey.finanzas.backend.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombreUsuario) = LOWER(:nombreUsuario)")
    Optional<Usuario> findByNombreUsuarioIgnoreCase(@Param("nombreUsuario") String nombreUsuario);
    Optional<Usuario> findByCorreoElectronicoIgnoreCase(String correo);
    @Query(value = "SELECT COUNT(*) > 0 FROM usuarios WHERE LOWER(TRIM(nombre_usuario)) = LOWER(TRIM(:nombreUsuario))", nativeQuery = true)
    boolean existsNombreUsuarioEnTodaLaTabla(@Param("nombreUsuario") String nombreUsuario);
}
