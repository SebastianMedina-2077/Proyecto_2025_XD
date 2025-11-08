package Clases_Tienda;

import DB_Conection.EntidadBase;

public class Almacen extends EntidadBase {
    private String codigoAlmacen;
    private String nombreAlmacen;
    private String direccionAlmacen;

    public Almacen() {
        super();
    }

    public Almacen(String codigoAlmacen, String nombreAlmacen) {
        super();
        this.codigoAlmacen = codigoAlmacen;
        this.nombreAlmacen = nombreAlmacen;
    }

    public Almacen(String codigoAlmacen, String nombreAlmacen, String direccionAlmacen) {
        this(codigoAlmacen, nombreAlmacen);
        this.direccionAlmacen = direccionAlmacen;
    }

    @Override
    public String getNombre() {
        return nombreAlmacen;
    }

    @Override
    public boolean isActivo() {
        return true;
    }

    // Almacenes predefinidos
    public static Almacen ALMACEN_PRINCIPAL() {
        return new Almacen("ALM01", "Almacén Principal", "Dirección Principal 123");
    }

    public static Almacen ALMACEN_SECUNDARIO() {
        return new Almacen("ALM02", "Almacén Secundario", "Dirección Secundaria 456");
    }

    // Getters y Setters
    public String getCodigoAlmacen() { return codigoAlmacen; }
    public void setCodigoAlmacen(String codigoAlmacen) {
        this.codigoAlmacen = codigoAlmacen;
        actualizarFechaModificacion();
    }

    public String getNombreAlmacen() { return nombreAlmacen; }
    public void setNombreAlmacen(String nombreAlmacen) {
        this.nombreAlmacen = nombreAlmacen;
        actualizarFechaModificacion();
    }

    public String getDireccionAlmacen() { return direccionAlmacen; }
    public void setDireccionAlmacen(String direccionAlmacen) {
        this.direccionAlmacen = direccionAlmacen;
        actualizarFechaModificacion();
    }
}
