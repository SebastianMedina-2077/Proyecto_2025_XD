package Interface;

import Catalogos.Estado;

public interface EntidadConEstado {
    Estado getEstado();
    void setEstado(Estado estado);
    boolean estaActivo();
}
