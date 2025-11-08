package Clases_Tienda;

import DB_Conection.EntidadBase;

public class Categoria extends EntidadBase {
    private String nombreCategoria;

    public Categoria() {
        super();
    }

    public Categoria(String nombreCategoria) {
        super();
        this.nombreCategoria = nombreCategoria;
    }

    public Categoria(Integer id, String nombreCategoria) {
        super(id);
        this.nombreCategoria = nombreCategoria;
    }

    @Override
    public String getNombre() {
        return nombreCategoria;
    }

    @Override
    public boolean isActivo() {
        return true;
    }

    // Métodos de negocio
    public boolean esCategoriaAlimentaria() {
        return nombreCategoria != null &&
                (nombreCategoria.toLowerCase().contains("alimento") ||
                        nombreCategoria.toLowerCase().contains("bebida") ||
                        nombreCategoria.toLowerCase().contains("comida"));
    }

    // Getters y Setters
    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
        actualizarFechaModificacion();
    }
}
