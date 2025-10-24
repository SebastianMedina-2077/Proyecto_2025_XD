package DB_Conection.Validaciones;

public interface Validarcampovacio {
    default boolean ValidarCampoVacio(String campo, String nombreCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            mostrarMensajeError("El campo " + nombreCampo + " no puede estar vacío");
            return false;
        }
        return true;
    }

    public abstract void mostrarMensajeError(String mensaje);
    public abstract void mostrarMensajeInformacion(String mensaje);
    public abstract void mostrarMensajeExito(String mensaje);
    public abstract void mostrarMensajeAdvertencia(String mensaje);
}
