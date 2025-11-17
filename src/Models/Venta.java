package Models;

import State.EstadoVenta;
import State.VentaPendiente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int idVenta;
    private String numeroVenta;
    private int idCliente;
    private int idUsuario;
    private int idMedioPago;
    private LocalDateTime fechaVenta;
    private double subtotal;
    private double descuentoTotal;
    private double igv;
    private double total;
    private String modalidadVenta;
    private String observaciones;
    private String estado;

    // Relaciones
    private Cliente cliente;
    private List<DetalleVenta> detalles;

    private EstadoVenta estadoVenta = new VentaPendiente();

    public Venta() {
        this.detalles = new ArrayList<>();
        this.estado = "Completada";
    }

    public void agregarDetalle(DetalleVenta detalle) {
        detalles.add(detalle);
        calcularTotales();
    }

    public void removerDetalle(DetalleVenta detalle) {
        detalles.remove(detalle);
        calcularTotales();
    }

    public void setEstadoVenta(EstadoVenta estado) {
        this.estadoVenta = estado;
        this.estado = estado.getNombreEstado();
    }

    private void calcularTotales() {
        this.subtotal = detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();

        this.descuentoTotal = detalles.stream()
                .mapToDouble(d -> d.getDescuentoPromocion() + d.getDescuentoAdicional())
                .sum();

        this.igv = subtotal * 0.18;
        this.total = subtotal + igv;
    }

    public int getIdVenta() { return idVenta; }
    public String getNumeroVenta() { return numeroVenta; }
    public int getIdCliente() { return idCliente; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdMedioPago() { return idMedioPago; }
    public LocalDateTime getFechaVenta() { return fechaVenta; }
    public double getSubtotal() { return subtotal; }
    public double getDescuentoTotal() { return descuentoTotal; }
    public double getIgv() { return igv; }
    public double getTotal() { return total; }
    public String getModalidadVenta() { return modalidadVenta; }
    public String getObservaciones() { return observaciones; }
    public String getEstado() { return estado; }
    public Cliente getCliente() { return cliente; }
    public List<DetalleVenta> getDetalles() { return detalles; }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public void setNumeroVenta(String numeroVenta) {
        this.numeroVenta = numeroVenta;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdMedioPago(int idMedioPago) {
        this.idMedioPago = idMedioPago;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public void setDescuentoTotal(double descuentoTotal) {
        this.descuentoTotal = descuentoTotal;
    }

    public void setIgv(double igv) {
        this.igv = igv;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setModalidadVenta(String modalidadVenta) {
        this.modalidadVenta = modalidadVenta;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public EstadoVenta getEstadoVenta() {
        return estadoVenta;
    }

    public void procesarVenta() {
        estadoVenta.procesar(this);
    }

    public void pagarVenta() {
        estadoVenta.pagar(this);
    }

    public void anularVenta() {
        estadoVenta.anular(this);
    }

    public void completarVenta() {
        estadoVenta.completar(this);
    }
}
