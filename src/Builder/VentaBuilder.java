package Builder;
import Clases.Cliente;
import Clases.Usuario;
import Clases_Tienda.Almacen;
import Enum.MetodoPago;
import Clases_Tienda.Producto;
import Clases_Tienda.VentaCabecera;
import Clases_Tienda.VentaDetalle;

import java.math.BigDecimal;

public class VentaBuilder {
    private VentaCabecera venta = new VentaCabecera();

    public VentaBuilder cliente(Cliente cliente) {
        venta.setCliente(cliente);
        return this;
    }

    public VentaBuilder vendedor(Usuario vendedor) {
        venta.setVendedor(vendedor);
        return this;
    }

    public VentaBuilder metodoPago(MetodoPago metodo) {
        venta.setMetodoPago(metodo);
        return this;
    }

    public VentaBuilder observaciones(String observaciones) {
        venta.setObservaciones(observaciones);
        return this;
    }

    public VentaBuilder agregarDetalle(Producto producto, int cantidad, BigDecimal precio) {
        return agregarDetalle(producto, cantidad, precio, BigDecimal.ZERO, Almacen.ALMACEN_PRINCIPAL());
    }

    public VentaBuilder agregarDetalle(Producto producto, int cantidad, BigDecimal precio,
                                       BigDecimal descuento, Almacen almacen) {
        VentaDetalle detalle = new VentaDetalle(producto, cantidad, precio, descuento, almacen);
        venta.agregarDetalle(detalle);
        return this;
    }

    public VentaCabecera construir() {
        if (venta.getCliente() == null || venta.getVendedor() == null || venta.getMetodoPago() == null) {
            throw new IllegalStateException("Faltan campos obligatorios para construir la venta");
        }
        return venta;
    }
}
