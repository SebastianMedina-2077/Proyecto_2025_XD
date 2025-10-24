package Clases_DAO;

import Catalogos.Estado;
import Catalogos.Tipo;
import Clases.Contacto;
import Clases.Usuario;
import DB_Conection.DAO;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class UsuarioDAO extends DAO<Usuario> {
    private static final String LISTAR_TODO_PROC = "SP_ListarUsuarios";
    private static final String REGISTRAR_PROC = "SP_RegistrarUsuario";

    @Override
    public Usuario parsear(ResultSet rs) throws SQLException {
        // Campos de USUARIOS_SM
        int idUsuario = rs.getInt("ID_USUARIO");
        String nombreUsuario = rs.getString("NOMBRE_US");
        String contrasena = rs.getString("CONTRASEÑA_US");
        String dni = rs.getString("DNI_SM");
        Timestamp fechaRegistroTs = rs.getTimestamp("FECHA_REGISTRO_US");
        LocalDateTime fechaRegistro = fechaRegistroTs != null ? fechaRegistroTs.toLocalDateTime() : null;
        Timestamp ultimoIngresoTs = rs.getTimestamp("ULTIMO_INGRESO");
        LocalDateTime ultimoIngreso = ultimoIngresoTs != null ? ultimoIngresoTs.toLocalDateTime() : null;
        int intentosFallidos = rs.getInt("INTENTOS_FALLIDOS");

        // Campos de ESTADO_SM
        String descripcionEstado = rs.getString("DESCRIPCION_ESTADO");
        boolean estadoValor = rs.getBoolean("ESTADO_ESTADO"); // Asumiendo que '1' es true, '0' es false
        Estado estado = new Estado(descripcionEstado, estadoValor);

        // Campos de TIPO_SM
        String nombreTipo = rs.getString("NOMBRE_TIP");
        String categoriaTipo = rs.getString("CATEGORIA_TIP");
        boolean activoTipo = rs.getBoolean("ESTADO_TIP"); // Asumiendo que '1' es true, '0' es false
        Tipo tipo = new Tipo(nombreTipo, categoriaTipo, activoTipo);

        // Campos de PERSONA_SM (heredados)
        String nombre = rs.getString("NOMBRE_SM");
        String apellido = rs.getString("APELLIDO_SM");
        Date fechaNacimiento = rs.getDate("FECHA_NACIMIENTO");
        String genero = rs.getString("GENERO");

        Contacto contacto = null;

        return new Usuario(dni, nombre, apellido, fechaNacimiento, genero, contacto, estado, tipo,
                nombreUsuario, contrasena, ultimoIngreso, intentosFallidos);
    }

    public boolean registrarUsuario(Usuario usuario, String contrasenaEncriptada, int idTipo, int idUsuarioAdmin) {

        boolean exito = false;
        String mensajeError = "";
        int resultado = 0;

        try (Connection conn = getconection();
             CallableStatement cs = conn.prepareCall("{CALL " + REGISTRAR_PROC + "(?, ?, ?, ?, ?, ?, ?)}")) {

            // Establecer parámetros del procedimiento
            cs.setString(1, usuario.getNombreUsuario());
            cs.setString(2, contrasenaEncriptada); // Usar la contraseña ya encriptada
            cs.setString(3, usuario.getDni());
            cs.setInt(4, idTipo); // ID del tipo de usuario (ej: 2 para EMPLEADO)
            cs.setInt(5, idUsuarioAdmin); // ID del usuario que realiza la acción

            // Parámetros de salida
            cs.registerOutParameter(6, Types.INTEGER); // @Resultado
            cs.registerOutParameter(7, Types.VARCHAR, 200); // @Mensaje

            // Ejecutar el procedimiento
            cs.execute();

            // Obtener resultados de salida
            resultado = cs.getInt(6);
            mensajeError = cs.getString(7);

            exito = (resultado == 1);
            if (!exito) {
                manejarError("Error del SP: " + mensajeError, null);
            } else {
                mensaje("Usuario registrado exitosamente.");
            }
        } catch (SQLException e) {
            manejarError("Error al registrar usuario", e);
        }
        return exito;
    }

    public List<Usuario> listarTodo() {
        return super.listarTodo(LISTAR_TODO_PROC);
    }
}
