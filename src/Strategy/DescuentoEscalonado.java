package Strategy;

import Models.Cliente;
import Models.DetalleVenta;

import java.util.List;

public class DescuentoEscalonado implements EstrategiaDescuento{
    @Override
    public double calcularDescuento(double subtotal, Cliente cliente, List<DetalleVenta> detalles) {
        double porcentaje = 0.0;

        if (subtotal >= 5000) {
            porcentaje = 20.0;
        } else if (subtotal >= 3000) {
            porcentaje = 15.0;
        } else if (subtotal >= 1000) {
            porcentaje = 10.0;
        } else if (subtotal >= 500) {
            porcentaje = 5.0;
        }

        return subtotal * (porcentaje / 100.0);
    }

    @Override
    public String getDescripcion() {
        return "Descuento escalonado por monto";
    }
}
