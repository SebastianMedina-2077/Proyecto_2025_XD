package Composite;

import Models.Producto;

public class ProductoSimple extends ComponenteProducto{
    private Producto producto;
    private int cantidad;

    public ProductoSimple(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.nombre = producto.getNombreProducto();
        this.precio = producto.getPrecioUnidad() * cantidad;
    }

    @Override
    public String obtenerNombre() {
        return producto.getNombreProducto() + " x" + cantidad;
    }

    @Override
    public double obtenerPrecio() {
        return producto.getPrecioUnidad() * cantidad;
    }

    @Override
    public void mostrarDetalle(String indent) {
        System.out.println(indent + "- " + obtenerNombre() +
                " (S/ " + obtenerPrecio() + ")");
    }

    @Override
    public boolean esCompuesto() {
        return false;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }
}
