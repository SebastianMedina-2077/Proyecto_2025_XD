package Decorator;

import Models.Producto;

public class GarantiaExtendidaDecorator extends ProductoDecorator{
    private int mesesGarantia;
    private double costoGarantia;

    public GarantiaExtendidaDecorator(Producto producto, int meses, double costo) {
        super(producto);
        this.mesesGarantia = meses;
        this.costoGarantia = costo;
    }

    @Override
    public String obtenerDescripcion() {
        return producto.getNombreProducto() + " + Garantia " + mesesGarantia + " meses";
    }

    @Override
    public double obtenerPrecio() {
        return producto.getPrecioUnidad() + costoGarantia;
    }

    public int getMesesGarantia() {
        return mesesGarantia;
    }

    public double getCostoGarantia() {
        return costoGarantia;
    }
}
