package Interface;

import Clases_Tienda.Producto;

import java.util.List;

public interface ProductoDAO extends GenericDAO<Producto, String> {
    List<Producto> buscarPorCategoria(String nombreCategoria);
    List<Producto> buscarPorMarca(String nombreMarca);
    List<Producto> productosConStockBajo();
    List<Producto> productosProximosVencer(int dias);
}
