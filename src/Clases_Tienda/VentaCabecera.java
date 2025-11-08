package Clases_Tienda;

import Clases.Cliente;
import Clases.Usuario;
import DB_Conection.EntidadBase;
import Interface.Auditable;
import Enum.EstadoVenta;
import Enum.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaCabecera extends EntidadBase implements Auditable {
    private String codigoFactura;
    private Cliente cliente;
    private Usuario vendedor;
    private LocalDateTime fechaVenta;
    private BigDecimal subtotal;
    private BigDecimal igvTotal;
    private BigDecimal descuentoTotal;
    private BigDecimal totalVenta;
    private MetodoPago metodoPago;
    private EstadoVenta estadoVenta;
    private String qrVenta;
    private String observaciones;
    private List<VentaDetalle> detalles;

    // Constructor por defecto
    public VentaCabecera() {
        super();
        this.detalles = new ArrayList<>();
        this.estadoVenta = EstadoVenta.PENDIENTE;
        this.fechaVenta = LocalDateTime.now();
        this.subtotal = BigDecimal.ZERO;
        this.igvTotal = BigDecimal.ZERO;
        this.descuentoTotal = BigDecimal.ZERO;
        this.totalVenta = BigDecimal.ZERO;
    }

    // Constructor básico
    public VentaCabecera(Cliente cliente, Usuario vendedor, MetodoPago metodoPago) {
        this();
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.metodoPago = metodoPago;
        this.fechaVenta = LocalDateTime.now();
    }

    // Constructor completo
    public VentaCabecera(String codigoFactura, Cliente cliente, Usuario vendedor,
                         LocalDateTime fechaVenta, BigDecimal subtotal, BigDecimal igvTotal,
                         BigDecimal descuentoTotal, BigDecimal totalVenta, MetodoPago metodoPago,
                         EstadoVenta estadoVenta, String qrVenta, String observaciones) {
        this(cliente, vendedor, metodoPago);
        this.codigoFactura = codigoFactura;
        this.fechaVenta = fechaVenta;
        this.subtotal = subtotal;
        this.igvTotal = igvTotal;
        this.descuentoTotal = descuentoTotal;
        this.totalVenta = totalVenta;
        this.estadoVenta = estadoVenta;
        this.qrVenta = qrVenta;
        this.observaciones = observaciones;
    }

    // Constructor desde ID
    public VentaCabecera(String codigoFactura, Cliente cliente, Usuario vendedor,
                         MetodoPago metodoPago, String observaciones) {
        this(cliente, vendedor, metodoPago);
        this.codigoFactura = codigoFactura;
        this.observaciones = observaciones;
    }

    // Método polimórfico para agregar detalle
    public boolean agregarDetalle(VentaDetalle detalle) {
        // Validaciones específicas
        if (this.estadoVenta != EstadoVenta.PENDIENTE) {
            return false;
        }

        if (detalle == null || detalle.getProducto() == null || detalle.getCantidad() <= 0) {
            return false;
        }

        // Verificar si el producto ya está en la venta
        for (VentaDetalle detalleExistente : detalles) {
            if (detalleExistente.getProducto().getCodigoProducto().equals(
                    detalle.getProducto().getCodigoProducto()) &&
                    detalleExistente.getAlmacen().getCodigoAlmacen().equals(
                            detalle.getAlmacen().getCodigoAlmacen())) {

                // Si el producto ya existe, aumentar la cantidad
                Integer nuevaCantidad = detalleExistente.getCantidad() + detalle.getCantidad();
                detalleExistente.actualizarCantidad(nuevaCantidad);
                recalcularTotales();
                return true;
            }
        }

        // Si no existe, agregar como nuevo detalle
        detalle.setVenta(this);
        this.detalles.add(detalle);
        recalcularTotales();
        return true;
    }

    // Método para agregar múltiples detalles
    public boolean agregarDetalles(List<VentaDetalle> nuevosDetalles) {
        boolean todosExitosos = true;
        for (VentaDetalle detalle : nuevosDetalles) {
            if (!agregarDetalle(detalle)) {
                todosExitosos = false;
            }
        }
        return todosExitosos;
    }

    // Método para quitar un detalle
    public boolean quitarDetalle(Integer indiceDetalle) {
        if (this.estadoVenta != EstadoVenta.PENDIENTE ||
                indiceDetalle < 0 || indiceDetalle >= detalles.size()) {
            return false;
        }

        detalles.remove(indiceDetalle);
        recalcularTotales();
        return true;
    }

    // Método para quitar detalle por producto
    public boolean quitarDetalle(Producto producto, Almacen almacen) {
        if (this.estadoVenta != EstadoVenta.PENDIENTE) {
            return false;
        }

        boolean eliminado = detalles.removeIf(detalle ->
                detalle.getProducto().equals(producto) &&
                        detalle.getAlmacen().equals(almacen));

        if (eliminado) {
            recalcularTotales();
        }
        return eliminado;
    }

    // Recálculo de totales
    private void recalcularTotales() {
        this.subtotal = detalles.stream()
                .map(VentaDetalle::getSubtotalDetalle)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.descuentoTotal = detalles.stream()
                .map(VentaDetalle::getTotalDescuento)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calcular IGV (18%)
        this.igvTotal = subtotal.multiply(BigDecimal.valueOf(0.18));

        // Calcular total final
        this.totalVenta = subtotal.add(igvTotal).subtract(descuentoTotal);

        actualizarFechaModificacion();
    }

    // Método polimórfico para cambiar estado
    public boolean cambiarEstado(EstadoVenta nuevoEstado) {
        return switch (this.estadoVenta) {
            case PENDIENTE -> switch (nuevoEstado) {
                case COMPLETADA, CANCELADA -> true;
                default -> false;
            };
            case COMPLETADA -> false; // No se puede cambiar
            case CANCELADA -> false; // No se puede cambiar
            case ANULADA -> false; // No se puede cambiar
        };
    }

    // Método para completar la venta
    public boolean completarVenta() {
        if (this.estadoVenta != EstadoVenta.PENDIENTE || detalles.isEmpty()) {
            return false;
        }

        // Generar código si no existe
        if (this.codigoFactura == null || this.codigoFactura.trim().isEmpty()) {
            this.codigoFactura = generarCodigoFactura();
        }

        // Generar QR de venta
        this.qrVenta = generarQRVenta();

        this.estadoVenta = EstadoVenta.COMPLETADA;
        this.fechaVenta = LocalDateTime.now();
        actualizarFechaModificacion();

        return true;
    }

    // Método para cancelar venta
    public boolean cancelarVenta(String motivo) {
        if (this.estadoVenta != EstadoVenta.PENDIENTE) {
            return false;
        }

        this.estadoVenta = EstadoVenta.CANCELADA;
        if (motivo != null && !motivo.trim().isEmpty()) {
            this.observaciones = (this.observaciones != null ? this.observaciones + "\n" : "") +
                    "CANCELADA: " + motivo;
        }
        actualizarFechaModificacion();
        return true;
    }

    // Método para anular venta (solo administradores)
    public boolean anularVenta(String motivo, Usuario usuario) {
        if (this.estadoVenta != EstadoVenta.COMPLETADA || !usuario.esAdministrador()) {
            return false;
        }

        this.estadoVenta = EstadoVenta.ANULADA;
        if (motivo != null && !motivo.trim().isEmpty()) {
            this.observaciones = (this.observaciones != null ? this.observaciones + "\n" : "") +
                    "ANULADA: " + motivo;
        }
        actualizarFechaModificacion();
        return true;
    }

    // Métodos de generación
    private String generarCodigoFactura() {
        // Formato: V + 6 dígitos secuenciales
        int numero = (int)(Math.random() * 999999) + 1;
        return "V" + String.format("%06d", numero);
    }

    private String generarQRVenta() {
        // Generación básica de QR (en implementación real usar librería QR)
        return "QR_" + this.codigoFactura + "_" + System.currentTimeMillis();
    }

    // Métodos de utilidad
    public int getCantidadItems() {
        return detalles.size();
    }

    public int getCantidadTotalProductos() {
        return detalles.stream()
                .mapToInt(VentaDetalle::getCantidad)
                .sum();
    }

    public BigDecimal getPromedioPorItem() {
        int cantidadItems = getCantidadItems();
        if (cantidadItems == 0) return BigDecimal.ZERO;
        return totalVenta.divide(BigDecimal.valueOf(cantidadItems), 2, BigDecimal.ROUND_HALF_UP);
    }

    public boolean tieneProductosProximosVencer() {
        return detalles.stream()
                .anyMatch(detalle -> detalle.getProducto().estaProximoVencer(30));
    }

    public boolean estaCompleta() {
        return !detalles.isEmpty() && estadoVenta == EstadoVenta.COMPLETADA;
    }

    public boolean esVentaContado() {
        return metodoPago == MetodoPago.EFECTIVO || metodoPago == MetodoPago.TARJETA;
    }

    public boolean esVentaCredito() {
        return metodoPago == MetodoPago.CREDITO;
    }

    // Métodos de auditoría
    @Override
    public Usuario getUsuarioCreacion() {
        return vendedor;
    }

    public void setUsuarioCreacion(Usuario usuarioCreacion) {
        this.vendedor = usuarioCreacion;
    }

    @Override
    public Usuario getUsuarioModificacion() {
        return vendedor;
    }

    public void setUsuarioModificacion(Usuario usuarioModificacion) {
        this.vendedor = usuarioModificacion;
        actualizarFechaModificacion();
    }

    @Override
    public LocalDateTime getFechaCreacion() {
        return getFechaRegistro();
    }

    // Métodos abstractos de EntidadBase
    @Override
    public String getNombre() {
        return "Venta " + (codigoFactura != null ? codigoFactura : "Sin código");
    }

    @Override
    public boolean isActivo() {
        return estadoVenta == EstadoVenta.COMPLETADA;
    }

    // Getters y Setters
    public String getCodigoFactura() { return codigoFactura; }
    public void setCodigoFactura(String codigoFactura) {
        this.codigoFactura = codigoFactura;
        actualizarFechaModificacion();
    }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        actualizarFechaModificacion();
    }

    public Usuario getVendedor() { return vendedor; }
    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
        actualizarFechaModificacion();
    }

    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
        actualizarFechaModificacion();
    }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        actualizarFechaModificacion();
    }

    public BigDecimal getIgvTotal() { return igvTotal; }
    public void setIgvTotal(BigDecimal igvTotal) {
        this.igvTotal = igvTotal;
        actualizarFechaModificacion();
    }

    public BigDecimal getDescuentoTotal() { return descuentoTotal; }
    public void setDescuentoTotal(BigDecimal descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
        actualizarFechaModificacion();
    }

    public BigDecimal getTotalVenta() { return totalVenta; }
    public void setTotalVenta(BigDecimal totalVenta) {
        this.totalVenta = totalVenta;
        actualizarFechaModificacion();
    }

    public MetodoPago getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
        actualizarFechaModificacion();
    }

    public EstadoVenta getEstadoVenta() { return estadoVenta; }
    public void setEstadoVenta(EstadoVenta estadoVenta) {
        this.estadoVenta = estadoVenta;
        actualizarFechaModificacion();
    }

    public String getQrVenta() { return qrVenta; }
    public void setQrVenta(String qrVenta) {
        this.qrVenta = qrVenta;
        actualizarFechaModificacion();
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
        actualizarFechaModificacion();
    }

    public List<VentaDetalle> getDetalles() {
        return new ArrayList<>(detalles); // Retornar copia para encapsulación
    }
}
