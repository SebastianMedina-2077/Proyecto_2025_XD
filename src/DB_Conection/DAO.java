package DB_Conection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import raven.toast.Notifications;


public abstract class DAO<T> extends Validador {
    protected Connection getConnection() {
        return DatabaseConnection.getInstance().getConnection();
    }

    public List<T> listarTodo(String procedure) {
        List<T> lista = new ArrayList<>();
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall("{CALL " + procedure + "}");
             ResultSet rs = cs.executeQuery()) {
            while (rs.next()) {
                T objeto = parsear(rs);
                if (objeto != null) {
                    lista.add(objeto);
                }
            }
        } catch (Exception e) {
            manejarError("Error al listar todo", e);
        }
        return lista;
    }

    public List<T> listarPorId(long id, String procedure) {
        List<T> lista = new ArrayList<>();
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall("{CALL " + procedure + " ?}")) {
            cs.setLong(1, id);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                T objeto = parsear(rs);
                if (objeto != null) {
                    lista.add(objeto);
                }
            }
            rs.close();
        } catch (Exception e) {
            manejarError("Error al listar por ID", e);
        }
        return lista;
    }

    protected void manejarError(String mensaje, Exception e) {
        System.err.println(mensaje + ": " + e.getMessage());
        if (e != null) e.printStackTrace();
        mostrarMensajeError(mensaje);
    }

    public abstract T parsear(ResultSet rs) throws SQLException;

    // Métodos auxiliares para ejecutar procedimientos
    protected boolean ejecutarProcedimiento(String procedure, Object... parametros) {
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall("{CALL " + procedure + "}")) {
            for (int i = 0; i < parametros.length; i++) {
                cs.setObject(i + 1, parametros[i]);
            }
            cs.execute();
            return true;
        } catch (Exception e) {
            manejarError("Error al ejecutar procedimiento", e);
            return false;
        }
    }
}
