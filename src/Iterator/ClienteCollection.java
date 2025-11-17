package Iterator;

import Models.Cliente;
import java.util.List;
import java.util.function.Predicate;

public class ClienteCollection {
    private List<Cliente> clientes;

    public ClienteCollection(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ClienteIterator createIterator() {
        return new ClienteIterator(clientes);
    }

    public ClienteIterator createFilteredIterator(Predicate<Cliente> filtro) {
        return new ClienteIterator(clientes, filtro);
    }

    public ClienteIterator createMayoristaIterator() {
        return createFilteredIterator(c -> "Mayorista".equals(c.getTipoCliente()));
    }

    public ClienteIterator createMinoristaIterator() {
        return createFilteredIterator(c -> "Minorista".equals(c.getTipoCliente()));
    }

    public ClienteIterator createEventualIterator() {
        return createFilteredIterator(c -> "Eventual".equals(c.getTipoCliente()));
    }

    public ClienteIterator createConDescuentoIterator() {
        return createFilteredIterator(c -> c.getDescuentoPersonalizado() > 0);
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public int size() {
        return clientes.size();
    }
}
