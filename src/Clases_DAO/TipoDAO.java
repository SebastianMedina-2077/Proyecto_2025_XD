package Clases_DAO;

import Catalogos.Tipo;
import DB_Conection.DAO;

import java.util.List;

public class TipoDAO extends DAO<Catalogos.Tipo> {
    private static final String LISTAR_TODO_PROC = "SP_ListarTipos";
    private static final String LISTAR_POR_ID_PROC = "SP_ListarTipoPorId";

    @Override
    public Catalogos.Tipo parsear(java.sql.ResultSet rs) throws java.sql.SQLException {
        int id = rs.getInt("ID_TIPO");
        String nombreTipo = rs.getString("NOMBRE_TIP");
        String categoria = rs.getString("CATEGORIA_TIPO");
        boolean activo = rs.getBoolean("ACTIVO");
        return new Catalogos.Tipo(nombreTipo, categoria, activo);
    }

    public List<Tipo> listarTodo() {
        return super.listarTodo(LISTAR_TODO_PROC);
    }

    public List<Catalogos.Tipo> listarPorId(long id) {
        return super.listarPorId(id, LISTAR_POR_ID_PROC);
    }

    public List<Tipo> listarTiposActivos() {
        List<Tipo> todos = listarTodo(LISTAR_TODO_PROC);
        // Filtrar aquí si el SP no lo hace
        //return todos.stream().filter(t -> t.getActivo()).toList();
        //return todos; // Si el SP ya filtra por ACTIVO = 1
        return super.listarTodo(todos.toString());
    }
}
