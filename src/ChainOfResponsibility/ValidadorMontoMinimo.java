package ChainOfResponsibility;

import Models.Venta;

public class ValidadorMontoMinimo extends ValidadorVenta{
    private static final double MONTO_MINIMO = 5.00;

    @Override
    public boolean validar(Venta venta) {
        System.out.println("Validando monto mínimo...");

        if (venta.getTotal() < MONTO_MINIMO) {
            System.err.println("El monto mínimo de venta es S/ " + MONTO_MINIMO);
            return false;
        }

        System.out.println("Monto validado");
        return pasarAlSiguiente(venta);
    }
}
