package Repository;

import DB_Conection.DAO;
import DB_Conection.DatabaseConnection;
import Models.Usuario;
import Models.ResultadoLogin;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends DAO<Usuario> {
    @Override
    public Usuario parsear(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("idUsuario"));
        usuario.setNombreUsuario(rs.getString("nombreUsuario"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setNombreCompleto(rs.getString("nombreCompleto"));
        usuario.setRol(rs.getString("rol"));
        usuario.setTelefono(rs.getString("telefono"));
        usuario.setEmail(rs.getString("email"));

        Timestamp ultimoAcceso = rs.getTimestamp("ultimoAcceso");
        usuario.setUltimoAcceso(ultimoAcceso != null ? ultimoAcceso.toLocalDateTime() : null);

        usuario.setIntentosFallidos(rs.getInt("intentosFallidos"));

        Timestamp bloqueadoHasta = rs.getTimestamp("bloqueadoHasta");
        usuario.setBloqueadoHasta(bloqueadoHasta != null ? bloqueadoHasta.toLocalDateTime() : null);

        Timestamp fechaCreacion = rs.getTimestamp("fechaCreacion");
        usuario.setFechaCreacion(fechaCreacion != null ? fechaCreacion.toLocalDateTime() : null);

        usuario.setEstado(rs.getString("estado"));

        return usuario;
    }

    // MÉTODO PRINCIPAL: Validar Login usando SP_ValidarLogin
    public ResultadoLogin validarLogin(String nombreUsuario, String contrasena, String ipAcceso) {
        ResultadoLogin resultado = new ResultadoLogin();

        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall(
                     "{CALL SP_ValidarLogin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {

            // Parámetros de entrada
            cs.setString(1, nombreUsuario);
            cs.setString(2, contrasena);
            cs.setString(3, ipAcceso);

            // Parámetros de salida
            cs.registerOutParameter(4, Types.BIT); // loginExitoso
            cs.registerOutParameter(5, Types.BIT); // usuarioBloqueado
            cs.registerOutParameter(6, Types.BIT); // usuarioInactivo
            cs.registerOutParameter(7, Types.BIT); // credencialesInvalidas
            cs.registerOutParameter(8, Types.INTEGER); // idUsuario
            cs.registerOutParameter(9, Types.VARCHAR); // nombreCompleto
            cs.registerOutParameter(10, Types.VARCHAR); // rol
            cs.registerOutParameter(11, Types.INTEGER); // minutosRestantesBloq

            cs.execute();

            // Leer resultados
            resultado.setLoginExitoso(cs.getBoolean(4));
            resultado.setUsuarioBloqueado(cs.getBoolean(5));
            resultado.setUsuarioInactivo(cs.getBoolean(6));
            resultado.setCredencialesInvalidas(cs.getBoolean(7));
            resultado.setIdUsuario(cs.getInt(8));
            resultado.setNombreCompleto(cs.getString(9));
            resultado.setRol(cs.getString(10));
            resultado.setMinutosRestantesBloq(cs.getInt(11));

        } catch (SQLException e) {
            manejarError("Error al validar login", e);
        }

        return resultado;
    }

    // Obtener información completa del usuario
    public Usuario obtenerPorId(int idUsuario) {
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall("{CALL SP_ObtenerInfoUsuario(?)}")) {

            cs.setInt(1, idUsuario);
            ResultSet rs = cs.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener usuario", e);
        }
        return null;
    }

    // Obtener usuario por nombre de usuario
    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM Usuario WHERE nombreUsuario = ? AND estado = 'Activo'")) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener usuario por nombre", e);
        }
        return null;
    }

    // Obtener usuario por email
    public Usuario obtenerPorEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM Usuario WHERE email = ? AND estado = 'Activo'")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener usuario por email", e);
        }
        return null;
    }

    // Insertar nuevo usuario
    public boolean insertar(Usuario usuario) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO Usuario (nombreUsuario, contrasena, nombreCompleto, rol, telefono, email) " +
                             "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getNombreCompleto());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getEmail());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
                mostrarMensajeExito("Usuario registrado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("unique")) {
                mostrarMensajeError("El nombre de usuario o email ya existe");
            } else {
                manejarError("Error al insertar usuario", e);
            }
        }
        return false;
    }

    // Actualizar usuario
    public boolean actualizar(Usuario usuario) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE Usuario SET nombreCompleto = ?, telefono = ?, email = ?, " +
                             "rol = ?, estado = ? WHERE idUsuario = ?")) {

            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getTelefono());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getEstado());
            ps.setInt(6, usuario.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                mostrarMensajeExito("Usuario actualizado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            manejarError("Error al actualizar usuario", e);
        }
        return false;
    }

    // Cambiar contraseña usando SP
    public boolean cambiarContrasena(int idUsuario, String contrasenaActual, String contrasenaNueva) {
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall(
                     "{CALL SP_CambiarContrasena(?, ?, ?, ?, ?)}")) {

            cs.setInt(1, idUsuario);
            cs.setString(2, contrasenaActual);
            cs.setString(3, contrasenaNueva);
            cs.registerOutParameter(4, Types.BIT); // cambioExitoso
            cs.registerOutParameter(5, Types.BIT); // contrasenaActualIncorrecta

            cs.execute();

            boolean exito = cs.getBoolean(4);
            boolean incorrecta = cs.getBoolean(5);

            if (exito) {
                mostrarMensajeExito("Contraseña cambiada exitosamente");
                return true;
            } else if (incorrecta) {
                mostrarMensajeError("Contraseña actual incorrecta");
            }
        } catch (SQLException e) {
            manejarError("Error al cambiar contraseña", e);
        }
        return false;
    }

    // Desbloquear usuario
    public boolean desbloquearUsuario(int idUsuarioAdmin, int idUsuarioBloqueado) {
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall(
                     "{CALL SP_DesbloquearUsuario(?, ?, ?, ?)}")) {

            cs.setInt(1, idUsuarioAdmin);
            cs.setInt(2, idUsuarioBloqueado);
            cs.registerOutParameter(3, Types.BIT); // desbloqueado
            cs.registerOutParameter(4, Types.BIT); // sinPermisos

            cs.execute();

            boolean desbloqueado = cs.getBoolean(3);
            boolean sinPermisos = cs.getBoolean(4);

            if (desbloqueado) {
                mostrarMensajeExito("Usuario desbloqueado exitosamente");
                return true;
            } else if (sinPermisos) {
                mostrarMensajeError("No tiene permisos para desbloquear usuarios");
            }
        } catch (SQLException e) {
            manejarError("Error al desbloquear usuario", e);
        }
        return false;
    }

    // Verificar permisos
    public boolean verificarPermiso(int idUsuario, String permiso) {
        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall(
                     "{CALL SP_VerificarPermiso(?, ?, ?)}")) {

            cs.setInt(1, idUsuario);
            cs.setString(2, permiso);
            cs.registerOutParameter(3, Types.BIT);

            cs.execute();

            return cs.getBoolean(3);
        } catch (SQLException e) {
            manejarError("Error al verificar permiso", e);
        }
        return false;
    }

    // Listar todos los usuarios
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM Usuario ORDER BY fechaCreacion DESC")) {

            while (rs.next()) {
                lista.add(parsear(rs));
            }
        } catch (SQLException e) {
            manejarError("Error al listar usuarios", e);
        }
        return lista;
    }

    // Verificar si existe usuario
    public boolean existeUsuario(String nombreUsuario) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COUNT(*) FROM Usuario WHERE nombreUsuario = ?")) {

            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            manejarError("Error al verificar existencia de usuario", e);
        }
        return false;
    }

    // Verificar si existe email
    public boolean existeEmail(String email) {
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT COUNT(*) FROM Usuario WHERE email = ?")) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            manejarError("Error al verificar existencia de email", e);
        }
        return false;
    }
}
