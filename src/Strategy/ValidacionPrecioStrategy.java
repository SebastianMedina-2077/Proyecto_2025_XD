package Strategy;

import Clases_Tienda.Producto;
import Interface.ValidacionStrategy;

import java.math.BigDecimal;

public class ValidacionPrecioStrategy implements ValidacionStrategy {
    private BigDecimal precioMinimo;

    public ValidacionPrecioStrategy(BigDecimal precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    @Override
    public boolean validar(Object objeto) {
        if (!(objeto instanceof Producto)) {
            return false;
        }
        Producto producto = (Producto) objeto;
        return producto.getPrecioVenta().compareTo(precioMinimo) >= 0;
    }

    @Override
    public String getMensajeError() {
        return "El precio de venta debe ser mayor o igual a: $" + precioMinimo;
    }
}
