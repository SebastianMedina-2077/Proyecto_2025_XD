package Clases_DAO;

import Catalogos.Estado;
import DB_Conection.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class EstadoDAO extends DAO<Estado> {
    private static final String LISTAR_TODO_PROC = "SP_ListarEstados";
    private static final String LISTAR_POR_ID_PROC = "SP_ListarEstadoPorId";
    @Override
    public Catalogos.Estado parsear(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID_ESTADO");
        String descripcion = rs.getString("DESCRIP_ESTA");
        boolean activo = rs.getBoolean("ACTIVO");
        // Asumiendo que Catalogos.Estado tiene un constructor o setters adecuados
        return new Estado(descripcion, activo); // Ajusta según tu constructor
    }

    public List<Estado> listarEstadosActivos() {
        List<Estado> todos = listarTodo(LISTAR_TODO_PROC);
        // Filtrar aquí si el SP no lo hace
        return todos.stream().filter(e -> e.isEstado()).collect(Collectors.toList());
        //return todos; // Si el SP ya filtra por ACTIVO = 1
    }

    public List<Estado> listarTodo() {
        return super.listarTodo(LISTAR_TODO_PROC);
    }

    public List<Estado> listarPorId(long id) {
        return super.listarPorId(id, LISTAR_POR_ID_PROC);
    }

}
