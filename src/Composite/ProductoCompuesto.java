package Composite;

import java.util.ArrayList;
import java.util.List;

public class ProductoCompuesto extends ComponenteProducto {
    private List<ComponenteProducto> componentes;
    private double descuentoKit;

    public ProductoCompuesto(String nombre, double descuentoKit) {
        this.nombre = nombre;
        this.componentes = new ArrayList<>();
        this.descuentoKit = descuentoKit;
    }

    public void agregarComponente(ComponenteProducto componente) {
        componentes.add(componente);
    }

    public void removerComponente(ComponenteProducto componente) {
        componentes.remove(componente);
    }

    @Override
    public String obtenerNombre() {
        return nombre + " (Kit)";
    }

    @Override
    public double obtenerPrecio() {
        double total = 0;
        for (ComponenteProducto componente : componentes) {
            total += componente.obtenerPrecio();
        }
        return total * (1 - descuentoKit / 100);
    }

    @Override
    public void mostrarDetalle(String indent) {
        System.out.println(indent + obtenerNombre() +
                " (S/ " + obtenerPrecio() + ")");
        for (ComponenteProducto componente : componentes) {
            componente.mostrarDetalle(indent + "  ");
        }
    }

    @Override
    public boolean esCompuesto() {
        return true;
    }

    public List<ComponenteProducto> getComponentes() {
        return componentes;
    }

    public double getDescuentoKit() {
        return descuentoKit;
    }
}
