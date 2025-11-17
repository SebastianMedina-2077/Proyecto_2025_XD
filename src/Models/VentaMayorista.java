package Models;

import Template_Method.ProcesoVenta;

public class VentaMayorista extends ProcesoVenta {
    private static final int CANTIDAD_MINIMA = 50;

    @Override
    protected boolean validarPreventa() {
        System.out.println("Iniciando venta mayorista...");
        return true;
    }

    @Override
    protected boolean seleccionarCliente() {
        System.out.println("Validando cliente mayorista...");
        if (venta.getCliente() != null &&
                !"Mayorista".equals(venta.getCliente().getTipoCliente())) {
            System.out.println("El cliente debe ser de tipo Mayorista");
            return false;
        }
        return true;
    }

    @Override
    protected boolean seleccionarProductos() {
        System.out.println("Seleccionando productos para venta mayorista...");
        return true;
    }

    @Override
    protected boolean validarStock() {
        int cantidadTotal = venta.getDetalles().stream()
                .mapToInt(DetalleVenta::getCantidad)
                .sum();

        if (cantidadTotal < CANTIDAD_MINIMA) {
            System.out.println("La cantidad mínima para venta mayorista es " + CANTIDAD_MINIMA);
            return false;
        }

        return super.validarStock();
    }

    @Override
    protected boolean seleccionarMedioPago() {
        System.out.println("Seleccionando medio de pago...");
        return true;
    }

    @Override
    protected boolean confirmarVenta() {
        System.out.println("Confirmando venta mayorista...");
        venta.setModalidadVenta("Mayorista");
        return true;
    }
}
