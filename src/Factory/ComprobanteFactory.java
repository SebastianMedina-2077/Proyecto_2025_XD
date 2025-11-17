package Factory;

import Models.Cliente;
import Models.Comprobante;
import Models.Venta;

public abstract class ComprobanteFactory {
    public abstract Comprobante crearComprobante();

    public Comprobante generarComprobante(Venta venta, Cliente cliente) {
        Comprobante comprobante = crearComprobante();
        comprobante.setCliente(cliente);
        comprobante.setFecha(venta.getFechaVenta());
        comprobante.setSubtotal(venta.getSubtotal());
        comprobante.setDescuento(venta.getDescuentoTotal());
        comprobante.setIgv(venta.getIgv());
        comprobante.setTotal(venta.getTotal());
        comprobante.setEstado("EMITIDO");
        comprobante.generarNumero();
        return comprobante;
    }
}
