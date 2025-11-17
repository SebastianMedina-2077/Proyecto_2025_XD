package State;

import Models.Venta;

public class VentaPendiente implements EstadoVenta {
    @Override
    public void procesar(Venta venta) {
        System.out.println("Procesando venta pendiente...");
        venta.setEstado("Procesando");
        venta.setEstadoVenta(new VentaProcesando());
    }

    @Override
    public void pagar(Venta venta) {
        System.out.println("No se puede pagar una venta pendiente. Debe procesarse primero.");
    }

    @Override
    public void anular(Venta venta) {
        System.out.println("Anulando venta pendiente...");
        venta.setEstado("Anulada");
        venta.setEstadoVenta(new VentaAnulada());
    }

    @Override
    public void completar(Venta venta) {
        System.out.println("No se puede completar una venta pendiente directamente.");
    }

    @Override
    public String getNombreEstado() {
        return "PENDIENTE";
    }
}
