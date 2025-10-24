package Clases_Tienda;

public class Categoria {
    private String nombreCategoria;
    private boolean estadoCategoria;

    public Categoria(String nombreCategoria, boolean estadoCategoria) {
        this.nombreCategoria = nombreCategoria;
        this.estadoCategoria = estadoCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public boolean isEstadoCategoria() {
        return estadoCategoria;
    }
}
