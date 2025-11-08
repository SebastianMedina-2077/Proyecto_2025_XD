package Clases_Tienda;

import java.math.BigDecimal;

public class VentaDetalle {
    private Integer idDetalle;
    private VentaCabecera venta;
    private Producto producto;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuentoUnitario;
    private BigDecimal subtotalDetalle;
    private Almacen almacen;

    // Constructor por defecto
    public VentaDetalle() {
        this.descuentoUnitario = BigDecimal.ZERO;
    }

    // Constructor básico
    public VentaDetalle(Producto producto, Integer cantidad, BigDecimal precioUnitario, Almacen almacen) {
        this();
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.almacen = almacen;
        this.subtotalDetalle = calcularSubtotal();
    }

    // Constructor con descuento
    public VentaDetalle(Producto producto, Integer cantidad, BigDecimal precioUnitario,
                        BigDecimal descuentoUnitario, Almacen almacen) {
        this(producto, cantidad, precioUnitario, almacen);
        this.descuentoUnitario = descuentoUnitario;
        this.subtotalDetalle = calcularSubtotal();
    }

    // Constructor completo
    public VentaDetalle(Integer idDetalle, Producto producto, Integer cantidad,
                        BigDecimal precioUnitario, BigDecimal descuentoUnitario,
                        BigDecimal subtotalDetalle, Almacen almacen) {
        this.idDetalle = idDetalle;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoUnitario = descuentoUnitario != null ? descuentoUnitario : BigDecimal.ZERO;
        this.subtotalDetalle = subtotalDetalle;
        this.almacen = almacen;
    }

    // Constructor con VentaCabecera
    public VentaDetalle(VentaCabecera venta, Producto producto, Integer cantidad,
                        BigDecimal precioUnitario, BigDecimal descuentoUnitario, Almacen almacen) {
        this(producto, cantidad, precioUnitario, descuentoUnitario, almacen);
        this.venta = venta;
    }

    private BigDecimal calcularSubtotal() {
        BigDecimal cantidadExacta = BigDecimal.valueOf(cantidad);
        BigDecimal descuentoTotal = descuentoUnitario.multiply(cantidadExacta);
        BigDecimal subtotalSinDescuento = precioUnitario.multiply(cantidadExacta);
        return subtotalSinDescuento.subtract(descuentoTotal);
    }

    // Métodos de negocio
    public BigDecimal getSubtotalDetalle() {
        return subtotalDetalle != null ? subtotalDetalle : calcularSubtotal();
    }

    public BigDecimal getTotalDescuento() {
        return descuentoUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public BigDecimal getPrecioSinDescuento() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public BigDecimal getPorcentajeDescuento() {
        if (precioUnitario.equals(BigDecimal.ZERO)) return BigDecimal.ZERO;
        return descuentoUnitario.multiply(BigDecimal.valueOf(100)).divide(precioUnitario, 2, BigDecimal.ROUND_HALF_UP);
    }

    public void actualizarCantidad(Integer nuevaCantidad) {
        this.cantidad = nuevaCantidad;
        this.subtotalDetalle = calcularSubtotal();
    }

    public void actualizarDescuento(BigDecimal nuevoDescuento) {
        this.descuentoUnitario = nuevoDescuento;
        this.subtotalDetalle = calcularSubtotal();
    }

    public boolean tieneDescuento() {
        return descuentoUnitario != null && descuentoUnitario.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean validarStock() {
        // Validación que debe ser implementada con acceso a inventario
        return cantidad > 0;
    }

    // Getters y Setters
    public Integer getIdDetalle() { return idDetalle; }
    public void setIdDetalle(Integer idDetalle) { this.idDetalle = idDetalle; }

    public VentaCabecera getVenta() { return venta; }
    public void setVenta(VentaCabecera venta) { this.venta = venta; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) {
        this.producto = producto;
        this.subtotalDetalle = calcularSubtotal();
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        this.subtotalDetalle = calcularSubtotal();
    }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotalDetalle = calcularSubtotal();
    }

    public BigDecimal getDescuentoUnitario() { return descuentoUnitario; }
    public void setDescuentoUnitario(BigDecimal descuentoUnitario) {
        this.descuentoUnitario = descuentoUnitario;
        this.subtotalDetalle = calcularSubtotal();
    }

    public Almacen getAlmacen() { return almacen; }
    public void setAlmacen(Almacen almacen) { this.almacen = almacen; }
}
