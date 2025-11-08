package Interface;

public interface ValidacionStrategy {
    boolean validar(Object objeto);
    String getMensajeError();
}
