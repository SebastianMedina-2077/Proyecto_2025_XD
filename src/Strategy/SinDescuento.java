package Strategy;

import Models.Cliente;
import Models.DetalleVenta;

import java.util.List;

public class SinDescuento implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double subtotal, Cliente cliente, List<DetalleVenta> detalles) {
        return 0.0;
    }

    @Override
    public String getDescripcion() {
        return "Sin descuento";
    }
}
