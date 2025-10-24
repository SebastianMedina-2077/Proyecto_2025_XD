package Clases_Tienda;

public class Almacen {
    private String codigoAlmacen;
    private String nombreAlmacen;
    private boolean estadoAlmacen;
    public Almacen() {
    }

    public Almacen(String codigoAlmacen, String nombreAlmacen, boolean estadoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
        this.nombreAlmacen = nombreAlmacen;
        this.estadoAlmacen = estadoAlmacen;
    }
    public String getCodigoAlmacen() {
        return codigoAlmacen;
    }
    public String getNombreAlmacen() {
        return nombreAlmacen;
    }
    public boolean isEstadoAlmacen() {
        return estadoAlmacen;
    }
}
