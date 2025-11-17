package Strategy;

import Models.Cliente;
import Models.DetalleVenta;

import java.util.List;

public class DescuentoPorTipoCliente implements EstrategiaDescuento {
    @Override
    public double calcularDescuento(double subtotal, Cliente cliente, List<DetalleVenta> detalles) {
        if (cliente == null) return 0.0;

        double porcentaje = 0.0;
        switch (cliente.getTipoCliente()) {
            case "Mayorista":
                porcentaje = 10.0;
                break;
            case "Minorista":
                porcentaje = 5.0;
                break;
            case "Eventual":
                porcentaje = 0.0;
                break;
        }

        return subtotal * (porcentaje / 100.0);
    }

    @Override
    public String getDescripcion() {
        return "Descuento por tipo de cliente";
    }
}
