package Decorator;

import Models.Producto;

public class OfertaEspecialDecorator extends ProductoDecorator {
    private String etiquetaOferta;

    public OfertaEspecialDecorator(Producto producto, String etiqueta) {
        super(producto);
        this.etiquetaOferta = etiqueta;
    }

    @Override
    public String obtenerDescripcion() {
        return producto.getNombreProducto() + " *" + etiquetaOferta + "*";
    }

    @Override
    public double obtenerPrecio() {
        return producto.getPrecioUnidad();
    }

    public String getEtiquetaOferta() {
        return etiquetaOferta;
    }
}
