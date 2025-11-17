package Decorator;

import Models.Producto;

public abstract class ProductoDecorator {
    protected Producto producto;

    public ProductoDecorator(Producto producto) {
        this.producto = producto;
    }

    public abstract String obtenerDescripcion();
    public abstract double obtenerPrecio();
}
