package Strategy;

import Models.Cliente;
import Models.DetalleVenta;

import java.util.List;

public class DescuentoPersonalizado implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double subtotal, Cliente cliente, List<DetalleVenta> detalles) {
        if (cliente == null || cliente.getDescuentoPersonalizado() == 0) {
            return 0.0;
        }
        return subtotal * (cliente.getDescuentoPersonalizado() / 100.0);
    }

    @Override
    public String getDescripcion() {
        return "Descuento personalizado del cliente";
    }
}
