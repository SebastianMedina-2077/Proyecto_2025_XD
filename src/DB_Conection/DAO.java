package DB_Conection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import raven.toast.Notifications;


public abstract class DAO<T> extends Validador {
    protected Connection getconection() {
        String url = "jdbc:sqlserver://localhost:1433;database=pos_db;encrypt=false";
        String username = "sa";
        String password = "12345";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            manejarError("Error al conectar a la base de datos", e);
        }
        return null;
    }

    public List<T> listarTodo(String procedure) {
        List<T> lista = new ArrayList<>();
        try (Connection con = getconection(); CallableStatement cs = con.prepareCall("EXEC " + procedure); ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                T objeto = parsear(rs);
                if (objeto != null) { // Asegurarse de no añadir nulls
                    lista.add(objeto);
                }
            }
        } catch (Exception e) {
            manejarError("Error al Listar Todo", e);
        }
        return lista;
    }

    protected void manejarError(String mensaje, Exception e) {
        System.err.println(mensaje + " : " + e.getMessage());
        if (e != null) e.printStackTrace(); // Para debugging
    }

    protected void mensaje(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Informacion", JOptionPane.INFORMATION_MESSAGE);
    }
    public  void mensajeDeError(String mensaje){
        Notifications.getInstance().show(Notifications.Type.ERROR, mensaje);
    }
    public abstract T parsear(ResultSet rs) throws SQLException;

    public List<T> listarPorId(long id, String procedure) {
        List<T> lista = new ArrayList<>();
        try (Connection con = getconection(); CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?")) {
            cs.setLong(1, id);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                T objeto = parsear(rs);
                if (objeto != null) { // Asegurarse de no añadir nulls
                    lista.add(objeto);
                }
            }
        } catch (Exception e) {
            manejarError("Error al Listar Por Id", e);
        }
        return lista;
    }
}
