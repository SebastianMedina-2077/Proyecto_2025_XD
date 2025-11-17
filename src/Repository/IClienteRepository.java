package Repository;

import Models.Cliente;
import java.util.List;

public interface IClienteRepository {
    List<Cliente> listarTodos();
    Cliente obtenerPorId(int id);
    boolean insertar(Cliente cliente);
    boolean actualizar(Cliente cliente);
    boolean eliminar(int id);
}
