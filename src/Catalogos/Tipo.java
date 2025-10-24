package Catalogos;

public class Tipo {
    public int idTipo;
    public String nombreTipo;
    public String Categoria;
    public boolean activo;
    public Tipo(String nombreTipo, String Categoria, boolean activo) {
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
        this.Categoria = Categoria;
        this.activo = activo;
    }

    public String getNombreTipo() {
        return nombreTipo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public boolean getActivo() {
        return activo;
    }
}
