package ChainOfResponsibility;

import Models.Venta;

public class ValidadorLimiteCredito extends ValidadorVenta{
    private static final double LIMITE_CREDITO = 1000.00;

    @Override
    public boolean validar(Venta venta) {
        System.out.println("Validando límite de crédito...");

        if (venta.getTotal() > LIMITE_CREDITO) {
            System.err.println("El monto de la venta excede el límite de crédito de S/ " + LIMITE_CREDITO);
            return false;
        }

        System.out.println("Límite de crédito validado");
        return pasarAlSiguiente(venta);
    }
}
