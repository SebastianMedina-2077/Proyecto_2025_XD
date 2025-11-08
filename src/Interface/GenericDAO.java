package Interface;

import java.util.List;

public interface GenericDAO <T, K> {
    T obtenerPorId(K id);
    List<T> listarTodos();
    List<T> listarPorCriterio(String criterio, Object valor);
    T guardar(T entidad);
    T actualizar(T entidad);
    boolean eliminar(K id);
    boolean existe(K id);
}
