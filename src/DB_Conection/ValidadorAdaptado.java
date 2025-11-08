package DB_Conection;

import DB_Conection.Validaciones.ValidarNumero;
import DB_Conection.Validaciones.ValidarTexto;
import DB_Conection.Validaciones.Validarcampovacio;
import raven.toast.Notifications;

public class ValidadorAdaptado implements ValidarTexto, ValidarNumero, Validarcampovacio {
    public void mostrarMensajeError(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.ERROR, mensaje);
    }

    public void mostrarMensajeInformacion(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.INFO, mensaje);
    }

    public void mostrarMensajeExito(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.SUCCESS, mensaje);
    }

    public void mostrarMensajeAdvertencia(String mensaje) {
        Notifications.getInstance().show(Notifications.Type.WARNING, mensaje);
    }

    // Métodos de validación genéricos para las nuevas clases
    public boolean validarObjetoNulo(Object objeto, String nombreObjeto) {
        if (objeto == null) {
            mostrarMensajeError("El objeto " + nombreObjeto + " no puede ser null");
            return false;
        }
        return true;
    }

    public boolean validarListaVacia(java.util.List<?> lista, String nombreLista) {
        if (lista == null || lista.isEmpty()) {
            mostrarMensajeAdvertencia("La lista " + nombreLista + " no puede estar vacía");
            return false;
        }
        return true;
    }

    public boolean validarId(Integer id, String nombreCampo) {
        if (id == null || id <= 0) {
            mostrarMensajeError("El campo " + nombreCampo + " debe ser un número positivo");
            return false;
        }
        return true;
    }
}
