package Interface;

import Clases.Cliente;

import java.util.List;

public interface ClienteDAO extends GenericDAO<Cliente, String> {
    Cliente buscarPorDni(String dni);
    List<Cliente> buscarPorTipo(String tipoCliente);
    List<Cliente> clientesConDescuentoEspecial();
}
