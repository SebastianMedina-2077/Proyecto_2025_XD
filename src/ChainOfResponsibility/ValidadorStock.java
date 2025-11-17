package ChainOfResponsibility;

import Models.DetalleVenta;
import Models.Producto;
import Models.Venta;

public class ValidadorStock extends ValidadorVenta{
    @Override
    public boolean validar(Venta venta) {
        System.out.println("Validando stock disponible...");

        for (DetalleVenta detalle : venta.getDetalles()) {
            Producto producto = detalle.getProducto();
            if (producto.getStockTotal() < detalle.getCantidad()) {
                System.err.println("Stock insuficiente para: " + producto.getNombreProducto());
                System.err.println("Disponible: " + producto.getStockTotal() +
                        ", Requerido: " + detalle.getCantidad());
                return false;
            }
        }

        System.out.println("Stock validado correctamente");
        return pasarAlSiguiente(venta);
    }
}
