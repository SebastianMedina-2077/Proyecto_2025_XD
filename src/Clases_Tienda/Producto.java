package Clases_Tienda;

import Catalogos.Estado;
import Clases.Proveedor;
import DB_Conection.EntidadBase;
import Interface.EntidadConEstado;

import javax.swing.text.DateFormatter;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Producto extends EntidadBase implements EntidadConEstado {
    private String codigoProducto;
    private String nombre;
    private String descripcion;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockMinimo;
    private String lote;
    private LocalDate fechaVencimiento;
    private String imagenPath;
    private Categoria categoria;
    private Marca marca;
    private Estado estado;
    private Proveedor proveedor;

    // Constructor por defecto
    public Producto() {
        super();
        this.estado = Estado.ACTIVO();
        this.stockMinimo = 5;
    }

    // Constructor básico
    public Producto(String codigoProducto, String nombre, BigDecimal precioCompra,
                    BigDecimal precioVenta, Categoria categoria, Marca marca, Proveedor proveedor) {
        super();
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.categoria = categoria;
        this.marca = marca;
        this.proveedor = proveedor;
        this.estado = Estado.ACTIVO();
        this.stockMinimo = 5;
    }

    // Constructor completo
    public Producto(String codigoProducto, String nombre, String descripcion,
                    BigDecimal precioCompra, BigDecimal precioVenta, Integer stockMinimo,
                    String lote, LocalDate fechaVencimiento, String imagenPath,
                    Categoria categoria, Marca marca, Estado estado, Proveedor proveedor) {
        this.codigoProducto = codigoProducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stockMinimo = stockMinimo != null ? stockMinimo : 5;
        this.lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.imagenPath = imagenPath;
        this.categoria = categoria;
        this.marca = marca;
        this.estado = estado != null ? estado : Estado.ACTIVO();
        this.proveedor = proveedor;
    }

    // Métodos abstractos/interface
    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean isActivo() {
        return estado != null && estado.estaActivo();
    }

    @Override
    public Estado getEstado() {
        return estado;
    }

    @Override
    public void setEstado(Estado estado) {
        this.estado = estado;
        actualizarFechaModificacion();
    }

    @Override
    public boolean estaActivo() {
        return isActivo();
    }

    // Métodos de negocio
    public BigDecimal calcularMargenGanancia() {
        if (precioCompra == null || precioVenta == null) return BigDecimal.ZERO;
        return precioVenta.subtract(precioCompra);
    }

    public BigDecimal calcularPorcentajeGanancia() {
        BigDecimal margen = calcularMargenGanancia();
        if (precioCompra == null || precioCompra.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
        return margen.multiply(BigDecimal.valueOf(100)).divide(precioCompra, 2, BigDecimal.ROUND_HALF_UP);
    }

    public boolean estaProximoVencer(int diasAnticipacion) {
        if (fechaVencimiento == null) return false;
        return LocalDate.now().plusDays(diasAnticipacion).isAfter(fechaVencimiento);
    }

    public boolean estaVencido() {
        if (fechaVencimiento == null) return false;
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    public boolean tieneStockBajo(int stockActual) {
        return stockActual <= stockMinimo;
    }

    public boolean tieneVencimiento() {
        return fechaVencimiento != null;
    }

    public int getDiasHastaVencimiento() {
        if (fechaVencimiento == null) return Integer.MAX_VALUE;
        return (int) LocalDate.now().until(fechaVencimiento).getDays();
    }

    public void actualizarPrecios(BigDecimal nuevoPrecioCompra, BigDecimal nuevoPrecioVenta) {
        this.precioCompra = nuevoPrecioCompra;
        this.precioVenta = nuevoPrecioVenta;
        actualizarFechaModificacion();
    }

    public void actualizarStockMinimo(Integer nuevoStockMinimo) {
        this.stockMinimo = nuevoStockMinimo;
        actualizarFechaModificacion();
    }

    // Getters y Setters
    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
        actualizarFechaModificacion();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
        actualizarFechaModificacion();
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        actualizarFechaModificacion();
    }

    public BigDecimal getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(BigDecimal precioCompra) {
        this.precioCompra = precioCompra;
        actualizarFechaModificacion();
    }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
        actualizarFechaModificacion();
    }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
        actualizarFechaModificacion();
    }

    public String getLote() { return lote; }
    public void setLote(String lote) {
        this.lote = lote;
        actualizarFechaModificacion();
    }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
        actualizarFechaModificacion();
    }

    public String getImagenPath() { return imagenPath; }
    public void setImagenPath(String imagenPath) {
        this.imagenPath = imagenPath;
        actualizarFechaModificacion();
    }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        actualizarFechaModificacion();
    }

    public Marca getMarca() { return marca; }
    public void setMarca(Marca marca) {
        this.marca = marca;
        actualizarFechaModificacion();
    }

    public Proveedor getProveedor() { return proveedor; }
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
        actualizarFechaModificacion();
    }
}
