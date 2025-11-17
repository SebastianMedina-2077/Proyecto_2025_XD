package Iterator;

import Models.Cliente;
import java.util.List;
import java.util.function.Predicate;


public class ClienteIterator implements Iterator<Cliente> {
    private List<Cliente> clientes;
    private int posicion;
    private Predicate<Cliente> filtro;

    public ClienteIterator(List<Cliente> clientes) {
        this.clientes = clientes;
        this.posicion = 0;
        this.filtro = null;
    }

    public ClienteIterator(List<Cliente> clientes, Predicate<Cliente> filtro) {
        this.clientes = clientes;
        this.posicion = 0;
        this.filtro = filtro;
        avanzarAProximoValido();
    }

    @Override
    public boolean hasNext() {
        if (filtro == null) {
            return posicion < clientes.size();
        }

        int temp = posicion;
        while (temp < clientes.size()) {
            if (filtro.test(clientes.get(temp))) {
                return true;
            }
            temp++;
        }
        return false;
    }

    @Override
    public Cliente next() {
        if (!hasNext()) {
            return null;
        }

        Cliente cliente = clientes.get(posicion);
        posicion++;
        avanzarAProximoValido();
        return cliente;
    }

    @Override
    public Cliente current() {
        if (posicion > 0 && posicion <= clientes.size()) {
            return clientes.get(posicion - 1);
        }
        return null;
    }

    @Override
    public void reset() {
        posicion = 0;
        avanzarAProximoValido();
    }

    private void avanzarAProximoValido() {
        if (filtro != null) {
            while (posicion < clientes.size() && !filtro.test(clientes.get(posicion))) {
                posicion++;
            }
        }
    }

    public int getPosition() {
        return posicion;
    }

    public int getTotalElements() {
        if (filtro == null) {
            return clientes.size();
        }

        int count = 0;
        for (Cliente cliente : clientes) {
            if (filtro.test(cliente)) {
                count++;
            }
        }
        return count;
    }
}
