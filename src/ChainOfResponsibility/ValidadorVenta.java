package ChainOfResponsibility;

import Models.Venta;

public abstract class ValidadorVenta {
    protected ValidadorVenta siguiente;

    public void setSiguiente(ValidadorVenta siguiente) {
        this.siguiente = siguiente;
    }

    public abstract boolean validar(Venta venta);

    protected boolean pasarAlSiguiente(Venta venta) {
        if (siguiente == null) {
            return true;
        }
        return siguiente.validar(venta);
    }
}
