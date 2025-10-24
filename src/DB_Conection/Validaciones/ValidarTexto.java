package DB_Conection.Validaciones;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JTextField;

public interface ValidarTexto  {
    default boolean ValidarCampoTexto(JTextField campo, String nombreCampo) {
        String regex = "^[a-zA-Z]+(\\s[a-zA-Z]+)*$";
        if (!campo.getText().matches(regex)) {
            mostrarMensajeError("El campo " + nombreCampo + " solo admite letras");
            return false;
        }
        return true;
    }

    public abstract void mostrarMensajeError(String mensaje);
    public abstract void mostrarMensajeInformacion(String mensaje);
    public abstract void mostrarMensajeExito(String mensaje);
    public abstract void mostrarMensajeAdvertencia(String mensaje);

    default boolean ValidarFormatoTextoConNumero(JTextField campo, String mensajeError) {
        String regex = "^[a-zA-Z](?: [a-zA-Z]+)*[0-9]*$";
        if (!campo.getText().matches(regex)) {
            mostrarMensajeError(mensajeError);
            return false;
        }
        return true;
    }

    default boolean ValidarFormatoCorreo(JTextField campo, String nombreCampo) {
        // Expresión regular para validar correos electrónicos
        String regex = "^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$";

        if (!campo.getText().matches(regex)) {
            mostrarMensajeAdvertencia("El formato de " + nombreCampo + " es incorrecto");
            return false;
        }
        return true;
    }

    default boolean validarFecha(JTextField campo, String nombreCampo) {
        String fechaTexto = campo.getText().trim();
        if (fechaTexto.isEmpty() || fechaTexto.contains("_")) {
            mostrarMensajeError("El campo " + nombreCampo + " no puede estar vacío o tener guiones bajos");
            return false;
        }
        try {
            // Intenta parsear la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(fechaTexto, formatter);
            return true;
        } catch (DateTimeParseException e) {
            mostrarMensajeInformacion("El formato de " + nombreCampo + " es incorrecto. Debe ser dd/MM/yyyy");
            return false;
        }
    }

    default boolean validarLongitud(JTextField campo, int min, int max, String mensajeError) {
        if (campo.getText().length() < min || campo.getText().length() > max) {
            mostrarMensajeAdvertencia(mensajeError);
            return false;
        } else {
            return true;
        }
    }

}
