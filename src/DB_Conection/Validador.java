package DB_Conection;
import DB_Conection.Validaciones.ValidarNumero;
import DB_Conection.Validaciones.ValidarTexto;
import DB_Conection.Validaciones.Validarcampovacio;
import raven.toast.Notifications;


public abstract class Validador implements  ValidarTexto, ValidarNumero, Validarcampovacio {
    
    public void mostrarMensajeError(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.ERROR,mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.INFO,mensaje);
    }

    public void mostrarMensajeExito(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.SUCCESS,mensaje);
    }

    public void mostrarMensajeAdvertencia(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.WARNING,mensaje);
    }
}
