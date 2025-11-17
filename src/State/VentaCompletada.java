package State;

import Models.Venta;

public class VentaCompletada implements EstadoVenta{
    @Override
    public void procesar(Venta venta) {
        System.out.println("La venta ya está completada.");
    }

    @Override
    public void pagar(Venta venta) {
        System.out.println("La venta ya está completada.");
    }

    @Override
    public void anular(Venta venta) {
        System.out.println("No se puede anular una venta completada sin autorización.");
    }

    @Override
    public void completar(Venta venta) {
        System.out.println("La venta ya está completada.");
    }

    @Override
    public String getNombreEstado() {
        return "COMPLETADA";
    }
}
