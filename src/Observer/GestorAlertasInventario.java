package Observer;

import Clases_Tienda.Almacen;
import Clases_Tienda.Producto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestorAlertasInventario implements ObservadorInventario{
    private List<String> alertas = new ArrayList<>();

    @Override
    public void notificarCambioStock(Producto producto, Almacen almacen, int stockAnterior, int stockNuevo) {
        String alerta = String.format("[%s] Stock actualizado: %s - %s (%d -> %d)",
                LocalDateTime.now(), producto.getNombre(), almacen.getNombre(), stockAnterior, stockNuevo);
        alertas.add(alerta);
        System.out.println("ALERTA: " + alerta);
    }

    @Override
    public void notificarStockBajo(Producto producto, Almacen almacen, int stockActual) {
        String alerta = String.format("[%s] STOCK BAJO: %s - %s (Stock actual: %d)",
                LocalDateTime.now(), producto.getNombre(), almacen.getNombre(), stockActual);
        alertas.add(alerta);
        System.out.println("ALERTA CRÍTICA: " + alerta);
    }

    @Override
    public void notificarProductoVencido(Producto producto, int diasVencimiento) {
        String alerta = String.format("[%s] PRODUCTO VENCIDO: %s (Vence en %d días)",
                LocalDateTime.now(), producto.getNombre(), diasVencimiento);
        alertas.add(alerta);
        System.out.println("ALERTA DE VENCIMIENTO: " + alerta);
    }

    public List<String> obtenerAlertas() {
        return new ArrayList<>(alertas);
    }

    public void limpiarAlertas() {
        alertas.clear();
    }
}
