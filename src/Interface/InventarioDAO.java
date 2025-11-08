package Interface;

import Clases_Tienda.Inventario;

import java.util.List;

public interface InventarioDAO extends GenericDAO<Inventario, String> {
    Inventario buscarPorProductoYAlmacen(String codigoProducto, String codigoAlmacen);
    List<Inventario> listarPorAlmacen(String codigoAlmacen);
    List<Inventario> listarStockBajo();
    boolean actualizarStock(String codigoProducto, String codigoAlmacen, int nuevoStock, Integer idUsuario);
}
