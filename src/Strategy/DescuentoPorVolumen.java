package Strategy;

import Models.Cliente;
import Models.DetalleVenta;

import java.util.List;

public class DescuentoPorVolumen implements EstrategiaDescuento{
    @Override
    public double calcularDescuento(double subtotal, Cliente cliente, List<DetalleVenta> detalles) {
        int cantidadTotal = detalles.stream()
                .mapToInt(DetalleVenta::getCantidad)
                .sum();

        double porcentaje = 0.0;
        if (cantidadTotal >= 100) {
            porcentaje = 15.0;
        } else if (cantidadTotal >= 50) {
            porcentaje = 10.0;
        } else if (cantidadTotal >= 20) {
            porcentaje = 5.0;
        }

        return subtotal * (porcentaje / 100.0);
    }

    @Override
    public String getDescripcion() {
        return "Descuento por volumen de compra";
    }
}
