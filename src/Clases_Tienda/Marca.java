package Clases_Tienda;

public class Marca {
    private String nombreMarca;
    private boolean estadoMarca;

    public Marca(String nombreMarca, boolean estadoMarca) {
        this.nombreMarca = nombreMarca;
        this.estadoMarca = estadoMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public boolean isEstadoMarca() {
        return estadoMarca;
    }
}
