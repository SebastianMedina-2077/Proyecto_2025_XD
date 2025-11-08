package Clases_Tienda;

import Clases.Usuario;
import DB_Conection.EntidadBase;
import Interface.Auditable;

import java.time.LocalDateTime;

public class Inventario extends EntidadBase implements Auditable {
    private Integer idInventario;
    private Producto producto;
    private Almacen almacen;
    private Integer stockActual;
    private Integer stockReservado;
    private Usuario usuarioModificacion;

    // Constructor por defecto
    public Inventario() {
        super();
        this.stockActual = 0;
        this.stockReservado = 0;
    }

    // Constructor básico con producto y almacén
    public Inventario(Producto producto, Almacen almacen) {
        super();
        this.producto = producto;
        this.almacen = almacen;
        this.stockActual = 0;
        this.stockReservado = 0;
    }

    // Constructor completo
    public Inventario(Integer idInventario, Producto producto, Almacen almacen,
                      Integer stockActual, Integer stockReservado, Usuario usuarioModificacion) {
        super(idInventario);
        this.idInventario = idInventario;
        this.producto = producto;
        this.almacen = almacen;
        this.stockActual = stockActual != null ? stockActual : 0;
        this.stockReservado = stockReservado != null ? stockReservado : 0;
        this.usuarioModificacion = usuarioModificacion;
    }

    // Constructor con stock inicial
    public Inventario(Producto producto, Almacen almacen, Integer stockInicial, Usuario usuario) {
        this(producto, almacen);
        this.stockActual = stockInicial;
        this.usuarioModificacion = usuario;
    }

    // Métodos de negocio
    public boolean tieneStockDisponible(int cantidad) {
        return (stockActual - stockReservado) >= cantidad;
    }

    public int getStockDisponible() {
        return stockActual - stockReservado;
    }

    public void actualizarStock(int nuevoStock, Usuario usuario) {
        this.stockActual = nuevoStock;
        this.usuarioModificacion = usuario;
        actualizarFechaModificacion();
    }

    public void reservarStock(int cantidad, Usuario usuario) {
        if (tieneStockDisponible(cantidad)) {
            this.stockReservado += cantidad;
            this.usuarioModificacion = usuario;
            actualizarFechaModificacion();
        }
    }

    public void liberarReserva(int cantidad) {
        this.stockReservado = Math.max(0, this.stockReservado - cantidad);
        actualizarFechaModificacion();
    }

    public void realizarEntrada(int cantidad, Usuario usuario) {
        this.stockActual += cantidad;
        this.usuarioModificacion = usuario;
        actualizarFechaModificacion();
    }

    public boolean realizarSalida(int cantidad, Usuario usuario) {
        if (tieneStockDisponible(cantidad)) {
            this.stockActual -= cantidad;
            this.usuarioModificacion = usuario;
            actualizarFechaModificacion();
            return true;
        }
        return false;
    }

    public boolean necesitaReposicion() {
        return producto != null && stockActual <= producto.getStockMinimo();
    }

    public void ajustarStock(int nuevoStock, String motivo, Usuario usuario) {
        int stockAnterior = this.stockActual;
        this.stockActual = nuevoStock;
        this.usuarioModificacion = usuario;
        actualizarFechaModificacion();

        // Crear movimiento de ajuste
        MovimientoInventario.movimientoAjuste(producto, almacen, stockAnterior, nuevoStock, motivo, usuario);
    }

    // Métodos de auditoría
    @Override
    public Usuario getUsuarioCreacion() {
        return null; // Se puede implementar si es necesario
    }

    @Override
    public Usuario getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuario usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
        actualizarFechaModificacion();
    }

    @Override
    public String getNombre() {
        return producto != null ? producto.getNombre() + " - " + almacen.getNombre() : "Inventario";
    }

    @Override
    public boolean isActivo() {
        return producto != null && producto.isActivo();
    }

    @Override
    public LocalDateTime getFechaCreacion() {
        return getFechaRegistro();
    }

    // Getters y Setters
    public Integer getIdInventario() { return idInventario != null ? idInventario : getId(); }
    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
        setId(idInventario);
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

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
        actualizarFechaModificacion();
    }

    public Integer getStockReservado() { return stockReservado; }
    public void setStockReservado(Integer stockReservado) {
        this.stockReservado = stockReservado;
        actualizarFechaModificacion();
    }
}
