package Observer;

import Clases_Tienda.Almacen;
import Clases_Tienda.Producto;

public interface SujetoInventario {
    void agregarObservador(ObservadorInventario observador);
    void removerObservador(ObservadorInventario observador);
    void notificarObservadores(Producto producto, Almacen almacen, String tipoNotificacion, Object datos);

}
