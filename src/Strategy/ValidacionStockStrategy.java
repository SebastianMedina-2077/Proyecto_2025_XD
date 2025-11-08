package Strategy;

import Clases_Tienda.Inventario;
import Interface.ValidacionStrategy;

public class ValidacionStockStrategy implements ValidacionStrategy {
    private int cantidadRequerida;

    public ValidacionStockStrategy(int cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    @Override
    public boolean validar(Object objeto) {
        if (!(objeto instanceof Inventario)) {
            return false;
        }
        Inventario inventario = (Inventario) objeto;
        return inventario.tieneStockDisponible(cantidadRequerida);
    }

    @Override
    public String getMensajeError() {
        return "Stock insuficiente para la cantidad requerida: " + cantidadRequerida;
    }
}
