package Decorator;

import Models.Producto;

public class DescuentoDecorator extends ProductoDecorator {
    private double porcentajeDescuento;
    private String motivoDescuento;

    public DescuentoDecorator(Producto producto, double porcentaje, String motivo) {
        super(producto);
        this.porcentajeDescuento = porcentaje;
        this.motivoDescuento = motivo;
    }

    @Override
    public String obtenerDescripcion() {
        return producto.getNombreProducto() + " [DESCUENTO " +
                porcentajeDescuento + "% - " + motivoDescuento + "]";
    }

    @Override
    public double obtenerPrecio() {
        return producto.getPrecioUnidad() * (1 - porcentajeDescuento / 100);
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public String getMotivoDescuento() {
        return motivoDescuento;
    }
}
