package ChainOfResponsibility;

import Models.Venta;

public class ValidadorMedioPago extends ValidadorVenta{
    @Override
    public boolean validar(Venta venta) {
        System.out.println("Validando medio de pago...");

        if (venta.getIdMedioPago() <= 0) {
            System.err.println("Debe seleccionar un medio de pago");
            return false;
        }

        System.out.println("Medio de pago validado");
        return pasarAlSiguiente(venta);
    }
}
