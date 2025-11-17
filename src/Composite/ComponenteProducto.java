package Composite;

import Models.Producto;
import java.util.ArrayList;
import java.util.List;

public abstract class ComponenteProducto {
    protected String nombre;
    protected double precio;

    public abstract String obtenerNombre();
    public abstract double obtenerPrecio();
    public abstract void mostrarDetalle(String indent);
    public abstract boolean esCompuesto();
}
