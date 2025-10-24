package DB_Conection;

public class Resultado<T> {
    private boolean exito;           // Indica si la operación fue exitosa
    private String mensaje;          // Mensaje descriptivo del resultado
    private T dato;                  // Dato retornado (puede ser null)
    private int codigo;
    public Resultado(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.dato = null;
        this.codigo = exito ? 1 : 0;
    }

}
