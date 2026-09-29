package mousebrey.finanzas.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "usuarios")
public class Usuario extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_id_seq")
    @SequenceGenerator(name = "usuario_id_seq", sequenceName = "usuario_id_seq", allocationSize = 1)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 100)
    private String nombreUsuario;

    @Column(name = "hash_contrasena", nullable = false, length = 255)
    private String hashContrasena;

    @Column(name = "nombre_completo", length = 200)
    private String nombreCompleto;

    @Column(name = "correo_electronico", length = 200)
    private String correoElectronico;

    @Column(name = "estado_registro")
    private Integer estadoRegistro = 1;

    @Column(name = "descripcion_baja", length = 500)
    private String descripcionBaja;

    @Column(name = "fecha_baja")
    private LocalDateTime fechaBaja;

    @Column(name = "usuario_baja", length = 100)
    private String usuarioBaja;
}
