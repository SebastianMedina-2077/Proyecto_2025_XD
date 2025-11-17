package Visitor;

import Models.Venta;

import java.util.List;

public interface ReporteVisitor {
    void visitarVenta(Venta venta);
    void visitarListaVentas(List<Venta> ventas);
    byte[] generarReporte() throws Exception;
}
