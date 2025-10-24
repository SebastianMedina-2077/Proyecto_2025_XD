package Catalogos;

public class Estado {
    public int id;
    public String descripcion;
    public boolean estado;

    public Estado(String descripcion, boolean estado) {
        this.estado = estado;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isEstado() {
        return estado;
    }
}
