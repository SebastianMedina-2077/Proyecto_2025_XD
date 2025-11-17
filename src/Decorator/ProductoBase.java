package Decorator;

import Models.Producto;

public class ProductoBase extends ProductoDecorator {
    public ProductoBase(Producto producto) {
        super(producto);
    }

    @Override
    public String obtenerDescripcion() {
        return producto.getNombreProducto();
    }

    @Override
    public double obtenerPrecio() {
        return producto.getPrecioUnidad();
    }
}
