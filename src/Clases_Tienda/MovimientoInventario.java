package Clases_Tienda;

import Clases.Usuario;

public abstract class MovimientoInventario {
    Producto producto;
    Almacen almacen;
    String tipomovimiento; // Entrada o Salida
    int cantidad;
    Usuario usuario;
    String referencia;
    String observacion;

    public MovimientoInventario(Producto producto, Almacen almacen, String tipomovimiento, int cantidad, Usuario usuario, String referencia, String observacion) {
        this.producto = producto;
        this.almacen = almacen;
        this.tipomovimiento = tipomovimiento;
        this.cantidad = cantidad;
        this.usuario = usuario;
        this.referencia = referencia;
        this.observacion = observacion;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipomovimiento() {
        return tipomovimiento;
    }

    public void setTipomovimiento(String tipomovimiento) {
        this.tipomovimiento = tipomovimiento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}
