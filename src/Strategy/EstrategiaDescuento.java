package Strategy;

import Models.Cliente;
import Models.DetalleVenta;

import java.util.List;

public interface EstrategiaDescuento {
    double calcularDescuento(double subtotal, Cliente cliente, List<DetalleVenta> detalles);
    String getDescripcion();
}
