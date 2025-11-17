package Observer;

import Models.Venta;

public interface ObservadorVenta {
    void actualizar(Venta venta, String evento);
}
