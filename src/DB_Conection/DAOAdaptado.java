package DB_Conection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

import Interface.GenericDAO;

public abstract class DAOAdaptado<T, K> extends ValidadorAdaptado implements GenericDAO<T, K> {
    protected Connection obtenerConexion() {
        String url = "jdbc:sqlserver://localhost:1433;database=pos_db;encrypt=false";
        String username = "sa";
        String password = "12345";
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            manejarError("Error al conectar a la base de datos", e);
            return null;
        }
    }

    protected void manejarError(String mensaje, Exception e) {
        System.err.println(mensaje + " : " + e.getMessage());
        if (e != null) e.printStackTrace();
    }

    protected void mostrarMensaje(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(null, mensaje, "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    // Implementación del patrón GenericDAO
    @Override
    public List<T> listarTodos() {
        String procedure = obtenerProcedureListarTodos();
        return ejecutarProcedureListar(procedure);
    }

    @Override
    public boolean existe(K id) {
        if (!validarId(convertirAInteger(id), "ID")) {
            return false;
        }

        T entidad = obtenerPorId(id);
        return entidad != null;
    }

    @Override
    public T obtenerPorId(K id) {
        if (!validarId(convertirAInteger(id), "ID")) {
            return null;
        }

        String procedure = obtenerProcedureListarPorId();
        List<T> resultados = ejecutarProcedureListarPorId(id, procedure);
        return resultados.isEmpty() ? null : resultados.get(0);
    }

    @Override
    public T guardar(T entidad) {
        if (!validarObjetoNulo(entidad, "entidad")) {
            return null;
        }

        if (!validarEntidad(entidad)) {
            return null;
        }

        try (Connection con = obtenerConexion()) {
            if (con == null) return null;

            String procedure = obtenerProcedureGuardar();
            return ejecutarProcedureGuardar(con, procedure, entidad);
        } catch (Exception e) {
            manejarError("Error al guardar entidad", e);
            return null;
        }
    }

    // Métodos para operaciones de eliminación y actualización
    public boolean eliminar(K id) {
        if (!validarId(convertirAInteger(id), "ID")) {
            return false;
        }

        try (Connection con = obtenerConexion()) {
            if (con == null) return false;

            String procedure = obtenerProcedureEliminar();
            return ejecutarProcedureEliminar(con, procedure, id);
        } catch (Exception e) {
            manejarError("Error al eliminar entidad", e);
            return false;
        }
    }

    public T actualizar(T entidad) {
        if (!validarObjetoNulo(entidad, "entidad")) {
            return null;
        }

        if (!validarEntidad(entidad)) {
            return null;
        }

        try (Connection con = obtenerConexion()) {
            if (con == null) return null;

            String procedure = obtenerProcedureActualizar();
            boolean resultado = ejecutarProcedureActualizar(con, procedure, entidad);

            // Si la actualización fue exitosa, retornar la entidad actualizada
            return resultado ? entidad : null;
        } catch (Exception e) {
            manejarError("Error al actualizar entidad", e);
            return null;
        }
    }

    @Override
    public List<T> listarPorCriterio(String criterio, Object valor) {
        if (!ValidarCampoVacio(criterio, "Criterio")) {
            return new ArrayList<>();
        }

        if (!validarObjetoNulo(valor, "Valor")) {
            return new ArrayList<>();
        }

        String procedure = obtenerProcedureBuscarPorCriterio();
        return ejecutarProcedureBuscarPorCriterio(criterio, valor, procedure);
    }

    // Métodos auxiliares que deben ser implementados por las subclases
    protected abstract String obtenerProcedureListarTodos();
    protected abstract String obtenerProcedureListarPorId();
    protected abstract String obtenerProcedureGuardar();
    protected abstract String obtenerProcedureEliminar();
    protected abstract String obtenerProcedureActualizar();
    protected abstract List<T> ejecutarProcedureListar(String procedure);
    protected abstract List<T> ejecutarProcedureListarPorId(K id, String procedure);
    protected abstract T ejecutarProcedureGuardar(Connection con, String procedure, T entidad);
    protected abstract boolean ejecutarProcedureEliminar(Connection con, String procedure, K id);
    protected abstract boolean ejecutarProcedureActualizar(Connection con, String procedure, T entidad);
    protected abstract String obtenerProcedureBuscarPorCriterio();
    protected abstract List<T> ejecutarProcedureBuscarPorCriterio(String criterio, Object valor, String procedure);

    protected abstract boolean validarEntidad(T entidad);

    // Método auxiliar para convertir IDs a Integer
    protected Integer convertirAInteger(K id) {
        if (id instanceof Integer) {
            return (Integer) id;
        } else if (id instanceof Long) {
            return ((Long) id).intValue();
        } else if (id instanceof String) {
            try {
                return Integer.parseInt((String) id);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    // Métodos reutilizables para operaciones comunes
    // Para procedures SIN parámetros
    protected List<T> ejecutarProcedureGenerico(String procedure, PreparedStatementCallback<T> callback) {
        return ejecutarProcedureGenericoConParametros(procedure, callback, 0);
    }

    // Para procedures CON parámetros - versión mejorada
    protected List<T> ejecutarProcedureGenericoConParametros(String procedure, PreparedStatementCallback<T> callback, int numParametros) {
        List<T> lista = new ArrayList<>();
        try (Connection con = obtenerConexion()) {
            if (con == null) return lista;

            // Construir la llamada con el número correcto de parámetros
            StringBuilder llamada = new StringBuilder("{CALL ").append(procedure);
            if (numParametros > 0) {
                llamada.append("(");
                for (int i = 0; i < numParametros; i++) {
                    llamada.append("?");
                    if (i < numParametros - 1) llamada.append(", ");
                }
                llamada.append(")");
            }
            llamada.append("}");

            try (CallableStatement cs = con.prepareCall(llamada.toString())) {
                callback.setParameters(cs);
                try (ResultSet rs = cs.executeQuery()) {
                    while (rs.next()) {
                        T objeto = callback.parsear(rs);
                        if (objeto != null) {
                            lista.add(objeto);
                        }
                    }
                }
            }
        } catch (Exception e) {
            manejarError("Error en operación genérica", e);
        }
        return lista;
    }

    protected interface PreparedStatementCallback<T> {
        void setParameters(CallableStatement cs) throws SQLException;
        T parsear(ResultSet rs) throws SQLException;
    }
}
