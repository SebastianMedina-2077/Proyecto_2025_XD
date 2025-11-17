package Models;

import Template_Method.ProcesoVenta;

public class VentaNormal extends ProcesoVenta {
    @Override
    protected boolean validarPreventa() {
        System.out.println("Iniciando venta normal...");
        return true;
    }

    @Override
    protected boolean seleccionarCliente() {
        // Aquí iría la lógica de selección del cliente
        System.out.println("Seleccionando cliente...");
        return true;
    }

    @Override
    protected boolean seleccionarProductos() {
        System.out.println("Seleccionando productos...");
        return true;
    }

    @Override
    protected boolean seleccionarMedioPago() {
        System.out.println("Seleccionando medio de pago...");
        return true;
    }

    @Override
    protected boolean confirmarVenta() {
        System.out.println("Confirmando venta...");
        return true;
    }
}
