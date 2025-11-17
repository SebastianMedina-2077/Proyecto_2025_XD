package State;

import Models.Venta;

public class VentaAnulada implements EstadoVenta{
    @Override
    public void procesar(Venta venta) {
        System.out.println("No se puede procesar una venta anulada.");
    }

    @Override
    public void pagar(Venta venta) {
        System.out.println("No se puede pagar una venta anulada.");
    }

    @Override
    public void anular(Venta venta) {
        System.out.println("La venta ya está anulada.");
    }

    @Override
    public void completar(Venta venta) {
        System.out.println("No se puede completar una venta anulada.");
    }

    @Override
    public String getNombreEstado() {
        return "ANULADA";
    }
}
