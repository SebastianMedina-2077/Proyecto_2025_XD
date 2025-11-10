package Memento;

import java.util.Stack;

public class HistorialUsuario {
    private Stack<UsuarioMemento> historial = new Stack<>();
    private static final int MAX_HISTORIAL = 10;

    public void guardar(UsuarioMemento memento) {
        if (historial.size() >= MAX_HISTORIAL) {
            historial.remove(0); // Eliminar el más antiguo
        }
        historial.push(memento);
    }

    public UsuarioMemento deshacer() {
        if (!historial.isEmpty()) {
            return historial.pop();
        }
        return null;
    }

    public boolean hayHistorial() {
        return !historial.isEmpty();
    }

    public int cantidadHistorial() {
        return historial.size();
    }

    public void limpiar() {
        historial.clear();
    }
}
