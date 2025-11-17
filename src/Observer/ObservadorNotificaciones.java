package Observer;

import Models.Venta;

public class ObservadorNotificaciones implements ObservadorVenta{
    @Override
    public void actualizar(Venta venta, String evento) {
        System.out.println("[Notificaciones] " + evento + ": " + venta.getNumeroVenta());

        if ("VENTA_REGISTRADA".equals(evento)) {
            // Enviar notificación por SMS o email
            System.out.println("Enviando notificación de venta registrada");
        }
    }
}
