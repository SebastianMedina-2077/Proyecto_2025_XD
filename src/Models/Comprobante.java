package Models;

import Models.Venta;
import Models.Cliente;
import java.time.LocalDateTime;


public abstract class Comprobante {
    protected String numeroComprobante;
    protected Cliente cliente;
    protected LocalDateTime fecha;
    protected double subtotal;
    protected double descuento;
    protected double igv;
    protected double total;
    protected String estado;

    public abstract String getTipoComprobante();
    public abstract boolean requiereRUC();
    public abstract void generarNumero();
    public abstract String obtenerFormatoImpresion();

    public String getNumeroComprobante() { return numeroComprobante; }
    public void setNumeroComprobante(String numero) { this.numeroComprobante = numero; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }
    public double getIgv() { return igv; }
    public void setIgv(double igv) { this.igv = igv; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
