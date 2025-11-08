package Clases_Tienda;

import Clases.Usuario;
import DB_Conection.EntidadBase;
import Interface.Auditable;
import java.time.LocalDateTime;
import Enum.TipoMovimiento;

public class MovimientoInventario extends EntidadBase implements Auditable {
    private Integer idMovimiento;
    private Producto producto;
    private Almacen almacen;
    private TipoMovimiento tipoMovimiento;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private Usuario usuarioMovimiento;
    private String referencia;
    private String observaciones;

    // Constructor por defecto
    public MovimientoInventario() {
        super();
    }

    // Constructor básico para movimientos
    public MovimientoInventario(Producto producto, Almacen almacen, TipoMovimiento tipoMovimiento,
                                Integer cantidad, Usuario usuarioMovimiento) {
        super();
        this.producto = producto;
        this.almacen = almacen;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.usuarioMovimiento = usuarioMovimiento;
    }

    // Constructor completo
    public MovimientoInventario(Integer idMovimiento, Producto producto, Almacen almacen,
                                TipoMovimiento tipoMovimiento, Integer cantidad, Integer stockAnterior,
                                Integer stockNuevo, Usuario usuarioMovimiento, String referencia,
                                String observaciones) {
        super(idMovimiento);
        this.idMovimiento = idMovimiento;
        this.producto = producto;
        this.almacen = almacen;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockNuevo = stockNuevo;
        this.usuarioMovimiento = usuarioMovimiento;
        this.referencia = referencia;
        this.observaciones = observaciones;
    }

    // Constructor para entradas
    public static MovimientoInventario movimientoEntrada(Producto producto, Almacen almacen,
                                                         Integer cantidad, Usuario usuario, String referencia) {
        MovimientoInventario movimiento = new MovimientoInventario(producto, almacen,
                TipoMovimiento.ENTRADA, cantidad, usuario);
        movimiento.setReferencia(referencia);
        return movimiento;
    }

    // Constructor para salidas
    public static MovimientoInventario movimientoSalida(Producto producto, Almacen almacen,
                                                        Integer cantidad, Usuario usuario, String referencia) {
        MovimientoInventario movimiento = new MovimientoInventario(producto, almacen,
                TipoMovimiento.SALIDA, cantidad, usuario);
        movimiento.setReferencia(referencia);
        return movimiento;
    }

    // Constructor para ajustes
    public static MovimientoInventario movimientoAjuste(Producto producto, Almacen almacen,
                                                        Integer stockAnterior, Integer stockNuevo,
                                                        String observaciones, Usuario usuario) {
        MovimientoInventario movimiento = new MovimientoInventario(producto, almacen,
                TipoMovimiento.AJUSTE, Math.abs(stockNuevo - stockAnterior), usuario);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockNuevo(stockNuevo);
        movimiento.setObservaciones(observaciones);
        return movimiento;
    }

    // Método polimórfico para validar movimiento
    public boolean validarMovimiento(Inventario inventario) {
        return switch (tipoMovimiento) {
            case ENTRADA -> true; // Siempre permitido
            case SALIDA -> inventario.tieneStockDisponible(cantidad);
            case AJUSTE -> true; // Siempre permitido (es ajuste manual)
        };
    }

    // Método para ejecutar el movimiento en el inventario
    public boolean ejecutarEnInventario(Inventario inventario) {
        if (!validarMovimiento(inventario)) {
            return false;
        }

        this.stockAnterior = inventario.getStockActual();

        switch (tipoMovimiento) {
            case ENTRADA:
                inventario.realizarEntrada(cantidad, usuarioMovimiento);
                this.stockNuevo = inventario.getStockActual();
                break;

            case SALIDA:
                boolean exito = inventario.realizarSalida(cantidad, usuarioMovimiento);
                this.stockNuevo = inventario.getStockActual();
                return exito;

            case AJUSTE:
                inventario.ajustarStock(stockNuevo, observaciones, usuarioMovimiento);
                break;
        }

        return true;
    }

    // Métodos de negocio
    public boolean esEntrada() {
        return tipoMovimiento == TipoMovimiento.ENTRADA;
    }

    public boolean esSalida() {
        return tipoMovimiento == TipoMovimiento.SALIDA;
    }

    public boolean esAjuste() {
        return tipoMovimiento == TipoMovimiento.AJUSTE;
    }

    public String getDescripcionCompleta() {
        return String.format("%s - %s: %d (Stock: %d -> %d)",
                tipoMovimiento.getDescripcion(),
                producto.getNombre(),
                cantidad,
                stockAnterior,
                stockNuevo);
    }

    // Métodos de auditoría
    @Override
    public Usuario getUsuarioCreacion() {
        return usuarioMovimiento;
    }

    public void setUsuarioCreacion(Usuario usuarioCreacion) {
        this.usuarioMovimiento = usuarioCreacion;
    }

    @Override
    public Usuario getUsuarioModificacion() {
        return usuarioMovimiento;
    }

    public void setUsuarioModificacion(Usuario usuarioModificacion) {
        this.usuarioMovimiento = usuarioModificacion;
        actualizarFechaModificacion();
    }

    @Override
    public LocalDateTime getFechaCreacion() {
        return getFechaRegistro();
    }

    // Métodos abstractos de EntidadBase
    @Override
    public String getNombre() {
        return producto != null ? producto.getNombre() : "Movimiento sin producto";
    }

    @Override
    public boolean isActivo() {
        return true; // Los movimientos siempre están activos
    }

    // Getters y Setters
    public Integer getIdMovimiento() { return idMovimiento != null ? idMovimiento : getId(); }
    public void setIdMovimiento(Integer idMovimiento) {
        this.idMovimiento = idMovimiento;
        setId(idMovimiento);
    }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) {
        this.producto = producto;
        actualizarFechaModificacion();
    }

    public Almacen getAlmacen() { return almacen; }
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
        actualizarFechaModificacion();
    }

    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
        actualizarFechaModificacion();
    }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        actualizarFechaModificacion();
    }

    public Integer getStockAnterior() { return stockAnterior; }
    public void setStockAnterior(Integer stockAnterior) { this.stockAnterior = stockAnterior; }

    public Integer getStockNuevo() { return stockNuevo; }
    public void setStockNuevo(Integer stockNuevo) { this.stockNuevo = stockNuevo; }

    public Usuario getUsuarioMovimiento() { return usuarioMovimiento; }
    public void setUsuarioMovimiento(Usuario usuarioMovimiento) { this.usuarioMovimiento = usuarioMovimiento; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) {
        this.referencia = referencia;
        actualizarFechaModificacion();
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        actualizarFechaModificacion();
    }
}
