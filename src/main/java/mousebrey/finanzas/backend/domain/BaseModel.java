package mousebrey.finanzas.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseModel {

    @Column(name = "USUARIO_CREACION")
    private String usuarioCreacion;

    @Column(name = "FECHA_CREACION")
    private LocalDateTime fechaCreacion;

    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDateTime fechaModificacion;
}
