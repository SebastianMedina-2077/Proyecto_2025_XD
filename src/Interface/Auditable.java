package Interface;

import Clases.Usuario;

import java.time.LocalDateTime;

public interface Auditable {
    Usuario getUsuarioCreacion();
    Usuario getUsuarioModificacion();
    LocalDateTime getFechaCreacion();
    LocalDateTime getFechaModificacion();
}
