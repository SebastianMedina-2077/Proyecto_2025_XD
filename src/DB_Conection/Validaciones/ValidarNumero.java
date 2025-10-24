package DB_Conection.Validaciones;

import javax.swing.JTextField;

public interface ValidarNumero {
    default boolean validarDNI(JTextField dni) {
        if (!dni.getText().matches("^[0-9]{8}$")) {  // El DNI debe tener exactamente 8 dígitos
            mostrarMensajeInformacion("El campo DNI debe contener 8 digitos");
            return false;
        }
        return true;
    }

    default boolean validarTelefono(JTextField telefono) {
        if (!telefono.getText().matches("^[0-9]{9}$")) {  // El teléfono debe tener exactamente 9 dígitos
            mostrarMensajeInformacion("El campo Teléfono debe contener 9 digitos");
            return false;
        }
        return true;
    }

    default boolean validarNumero(JTextField numero) {
        if (!numero.getText().matches("^[0-9]+$")) {  // El campo debe contener solo dígitos
            mostrarMensajeError("El campo debe contener solo números");
            return false;
        }
        return true;
    }

    default boolean validarPrecio(JTextField precio) {
        if (!precio.getText().matches("^[0-9]+(\\.[0-9]{1,2})?$")) {  // El precio debe ser un número positivo con hasta dos decimales
            mostrarMensajeAdvertencia("El campo Precio debe ser un número válido (ejemplo: 100.00)");
            return false;
        }
        return true;
    }
    public abstract void mostrarMensajeInformacion(String mensaje);
    public abstract void mostrarMensajeError(String mensaje);
    public abstract void mostrarMensajeExito(String mensaje);
    public abstract void mostrarMensajeAdvertencia(String mensaje);
}
