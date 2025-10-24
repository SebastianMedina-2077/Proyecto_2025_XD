package Clases_Tienda;

import Catalogos.Estado;
import Clases.Proveedor;

import javax.swing.text.DateFormatter;

public class Producto {
    private String codigoProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioCompra;
    private double precioVenta;
    private int stockMinimo;
    private String Lote;
    private DateFormatter fechaVencimiento;
    private String imagenProducto;
    private Categoria categoria;
    private Marca marca;
    private Estado estado;
    private Proveedor proveedor;

    public Producto(String codigoProducto, String nombreProducto, String descripcionProducto, double precioCompra, double precioVenta, int stockMinimo, String lote, DateFormatter fechaVencimiento, String imagenProducto, Categoria categoria, Marca marca, Estado estado, Proveedor proveedor) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stockMinimo = stockMinimo;
        Lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.imagenProducto = imagenProducto;
        this.categoria = categoria;
        this.marca = marca;
        this.estado = estado;
        this.proveedor = proveedor;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getLote() {
        return Lote;
    }

    public DateFormatter getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Marca getMarca() {
        return marca;
    }

    public Estado getEstado() {
        return estado;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }
}
