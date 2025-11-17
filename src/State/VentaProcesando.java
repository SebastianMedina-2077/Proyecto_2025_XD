package State;

import Models.Venta;

public class VentaProcesando implements EstadoVenta{
    @Override
    public void procesar(Venta venta) {
        System.out.println("La venta ya está en proceso.");
    }

    @Override
    public void pagar(Venta venta) {
        System.out.println("Registrando pago...");
        venta.setEstado("Pagada");
        venta.setEstadoVenta(new VentaPagada());
    }

    @Override
    public void anular(Venta venta) {
        System.out.println("Anulando venta en proceso...");
        venta.setEstado("Anulada");
        venta.setEstadoVenta(new VentaAnulada());
    }

    @Override
    public void completar(Venta venta) {
        System.out.println("No se puede completar sin pagar primero.");
    }

    @Override
    public String getNombreEstado() {
        return "PROCESANDO";
    }
}
