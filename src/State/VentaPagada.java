package State;

import Models.Venta;

public class VentaPagada implements EstadoVenta{
    @Override
    public void procesar(Venta venta) {
        System.out.println("La venta ya fue procesada.");
    }

    @Override
    public void pagar(Venta venta) {
        System.out.println("La venta ya está pagada.");
    }

    @Override
    public void anular(Venta venta) {
        System.out.println("Generando nota de crédito y anulando venta...");
        venta.setEstado("Anulada");
        venta.setEstadoVenta(new VentaAnulada());
    }

    @Override
    public void completar(Venta venta) {
        System.out.println("Completando venta...");
        venta.setEstado("Completada");
        venta.setEstadoVenta(new VentaCompletada());
    }

    @Override
    public String getNombreEstado() {
        return "PAGADA";
    }
}
