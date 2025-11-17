package Models;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idProducto;
    private int idAlmacen;
    private int cantidad;
    private String modalidadVenta;
    private double precioUnitario;
    private double descuentoPromocion;
    private double descuentoAdicional;
    private Integer idPromocionAplicada;
    private double subtotal;

    // Relación
    private Producto producto;

    public DetalleVenta(Producto producto, int cantidad, String modalidad, int almacen) {
        this.producto = producto;
        this.idProducto = producto.getIdProducto();
        this.cantidad = cantidad;
        this.modalidadVenta = modalidad;
        this.idAlmacen = almacen;
        this.precioUnitario = calcularPrecio(modalidad);
        this.subtotal = precioUnitario * cantidad;
    }

    private double calcularPrecio(String modalidad) {
        switch (modalidad) {
            case "Unidad": return producto.getPrecioUnidad();
            case "Docena": return producto.getPrecioDocena() / 12.0;
            case "Mayorista": return producto.getPrecioMayorista();
            default: return producto.getPrecioUnidad();
        }
    }

    public void aplicarDescuento(double descuento, String tipo) {
        if ("Promocion".equals(tipo)) {
            this.descuentoPromocion = descuento;
        } else {
            this.descuentoAdicional = descuento;
        }
        this.subtotal = (precioUnitario * cantidad) -
                descuentoPromocion - descuentoAdicional;
    }

    public int getIdDetalle() { return idDetalle; }
    public int getIdVenta() { return idVenta; }
    public int getIdProducto() { return idProducto; }
    public int getIdAlmacen() { return idAlmacen; }
    public int getCantidad() { return cantidad; }
    public String getModalidadVenta() { return modalidadVenta; }
    public double getPrecioUnitario() { return precioUnitario; }
    public double getDescuentoPromocion() { return descuentoPromocion; }
    public double getDescuentoAdicional() { return descuentoAdicional; }
    public Integer getIdPromocionAplicada() { return idPromocionAplicada; }
    public double getSubtotal() { return subtotal; }

    public Producto getProducto() { return producto; }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public void setIdAlmacen(int idAlmacen) {
        this.idAlmacen = idAlmacen;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setModalidadVenta(String modalidadVenta) {
        this.modalidadVenta = modalidadVenta;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setDescuentoPromocion(double descuentoPromocion) {
        this.descuentoPromocion = descuentoPromocion;
    }

    public void setDescuentoAdicional(double descuentoAdicional) {
        this.descuentoAdicional = descuentoAdicional;
    }

    public void setIdPromocionAplicada(Integer idPromocionAplicada) {
        this.idPromocionAplicada = idPromocionAplicada;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
