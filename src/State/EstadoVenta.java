package State;

import Models.Venta;

public interface EstadoVenta {
    void procesar(Venta venta);
    void pagar(Venta venta);
    void anular(Venta venta);
    void completar(Venta venta);
    String getNombreEstado();
}
