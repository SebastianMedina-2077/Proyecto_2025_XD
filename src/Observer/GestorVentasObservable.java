package Observer;

import Models.Venta;

import java.util.ArrayList;
import java.util.List;

public class GestorVentasObservable {
    private List<ObservadorVenta> observadores = new ArrayList<>();

    public void agregarObservador(ObservadorVenta observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorVenta observador) {
        observadores.remove(observador);
    }

    public void notificarObservadores(Venta venta, String evento) {
        for (ObservadorVenta observador : observadores) {
            observador.actualizar(venta, evento);
        }
    }

    public void registrarVenta(Venta venta) {
        // Lógica de registro...
        notificarObservadores(venta, "VENTA_REGISTRADA");
    }

    public void anularVenta(Venta venta) {
        // Lógica de anulación...
        notificarObservadores(venta, "VENTA_ANULADA");
    }

    public void completarVenta(Venta venta) {
        // Lógica de completar...
        notificarObservadores(venta, "VENTA_COMPLETADA");
    }
}
