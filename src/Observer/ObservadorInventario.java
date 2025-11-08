package Observer;

import Clases_Tienda.Almacen;
import Clases_Tienda.Producto;

public interface ObservadorInventario {
    void notificarCambioStock(Producto producto, Almacen almacen, int stockAnterior, int stockNuevo);
    void notificarStockBajo(Producto producto, Almacen almacen, int stockActual);
    void notificarProductoVencido(Producto producto, int diasVencimiento);
}
