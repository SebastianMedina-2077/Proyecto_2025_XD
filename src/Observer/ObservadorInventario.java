package Observer;

import Models.Venta;

public class ObservadorInventario implements ObservadorVenta{
    @Override
    public void actualizar(Venta venta, String evento) {
        if ("VENTA_REGISTRADA".equals(evento)) {
            System.out.println("[Inventario] Actualizando stock por venta: " +
                    venta.getNumeroVenta());
            // Aquí se actualizaría el inventario
        }
    }
}
