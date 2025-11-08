package Clases_Tienda;

import DB_Conection.EntidadBase;

public class Marca extends EntidadBase {
    private String nombreMarca;

    public Marca() {
        super();
    }

    public Marca(String nombreMarca) {
        super();
        this.nombreMarca = nombreMarca;
    }

    public Marca(Integer id, String nombreMarca) {
        super(id);
        this.nombreMarca = nombreMarca;
    }

    @Override
    public String getNombre() {
        return nombreMarca;
    }

    @Override
    public boolean isActivo() {
        return true;
    }

    // Getters y Setters
    public String getNombreMarca() { return nombreMarca; }
    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
        actualizarFechaModificacion();
    }
}
