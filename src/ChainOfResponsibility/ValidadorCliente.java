package ChainOfResponsibility;

import Models.Cliente;
import Models.Venta;

public class ValidadorCliente extends ValidadorVenta{
    @Override
    public boolean validar(Venta venta) {
        System.out.println("Validando cliente...");

        Cliente cliente = venta.getCliente();

        if (cliente == null) {
            System.err.println("Debe seleccionar un cliente");
            return false;
        }

        if (!"Activo".equals(cliente.getEstado())) {
            System.err.println("El cliente está inactivo");
            return false;
        }

        System.out.println("Cliente validado correctamente");
        return pasarAlSiguiente(venta);
    }
}
