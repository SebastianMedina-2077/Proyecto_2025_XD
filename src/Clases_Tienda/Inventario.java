package Clases_Tienda;

import Clases.Usuario;

public class Inventario {
    private Producto producto;
    private Almacen almacen;
    private int stockActual;
    private int stockReservado;
    private Usuario usuario;

    public Inventario(Producto producto, Almacen almacen, int stockActual, int stockReservado, Usuario usuario) {
        this.producto = producto;
        this.almacen = almacen;
        this.stockActual = stockActual;
        this.stockReservado = stockReservado;
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockReservado() {
        return stockReservado;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
