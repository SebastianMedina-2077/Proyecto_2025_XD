package Strategy;

import Clases_Tienda.Producto;
import Interface.ValidacionStrategy;

public class ValidacionVencimientoStrategy implements ValidacionStrategy {
    private int diasAnticipacion;

    public ValidacionVencimientoStrategy(int diasAnticipacion) {
        this.diasAnticipacion = diasAnticipacion;
    }

    @Override
    public boolean validar(Object objeto) {
        if (!(objeto instanceof Producto)) {
            return false;
        }
        Producto producto = (Producto) objeto;
        return !producto.estaProximoVencer(diasAnticipacion);
    }

    @Override
    public String getMensajeError() {
        return "Producto próximo a vencer en " + diasAnticipacion + " días";
    }
}
