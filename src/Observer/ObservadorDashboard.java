package Observer;

import Models.Venta;

public class ObservadorDashboard implements ObservadorVenta{
    private String nombreDashboard;

    public ObservadorDashboard(String nombre) {
        this.nombreDashboard = nombre;
    }

    @Override
    public void actualizar(Venta venta, String evento) {
        System.out.println("[Dashboard " + nombreDashboard + "] " + evento +
                ": Venta " + venta.getNumeroVenta() +
                " - Total: S/ " + venta.getTotal());
        // Aquí se actualizaría la interfaz gráfica del dashboard
    }
}
