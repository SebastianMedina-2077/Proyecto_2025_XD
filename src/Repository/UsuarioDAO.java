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

    public ResultadoLogin validarLogin(String nombreUsuario, String contrasena, String ipAcceso) {
        ResultadoLogin resultado = new ResultadoLogin();
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_ValidarLogin(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setString(1, nombreUsuario);
            cs.setString(2, contrasena);
            cs.setString(3, ipAcceso);

            cs.registerOutParameter(4, Types.BIT);
            cs.registerOutParameter(5, Types.BIT);
            cs.registerOutParameter(6, Types.BIT);
            cs.registerOutParameter(7, Types.BIT);
            cs.registerOutParameter(8, Types.INTEGER);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.VARCHAR);
            cs.registerOutParameter(11, Types.INTEGER);

            cs.execute();

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
        } finally {
            cerrarRecursos(cs, con);
        }

        return resultado;
    }

    public Usuario obtenerPorId(int idUsuario) {
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_ObtenerInfoUsuario(?)}");
            cs.setInt(1, idUsuario);
            rs = cs.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener usuario", e);
        } finally {
            cerrarRecursos(rs, cs, con);
        }

        return null;
    }

    public Usuario obtenerPorNombreUsuario(String nombreUsuario) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT * FROM Usuario WHERE nombreUsuario = ? AND estado = 'Activo'");
            ps.setString(1, nombreUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener usuario por nombre", e);
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return null;
    }

    public Usuario obtenerPorEmail(String email) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT * FROM Usuario WHERE email = ? AND estado = 'Activo'");
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener usuario por email", e);
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return null;
    }

    public boolean insertar(Usuario usuario) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "INSERT INTO Usuario (nombreUsuario, contrasena, nombreCompleto, rol, telefono, email) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getContrasena());
            ps.setString(3, usuario.getNombreCompleto());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getTelefono());
            ps.setString(6, usuario.getEmail());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }

                con.commit();

                mostrarMensajeExito("Usuario registrado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (e.getMessage().contains("unique") || e.getMessage().contains("UNIQUE")) {
                mostrarMensajeError("El nombre de usuario o email ya existe");
            } else {
                manejarError("Error al insertar usuario", e);
            }
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return false;
    }

    public boolean actualizar(Usuario usuario) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "UPDATE Usuario SET nombreCompleto = ?, telefono = ?, email = ?, " +
                            "rol = ?, estado = ? WHERE idUsuario = ?");

            ps.setString(1, usuario.getNombreCompleto());
            ps.setString(2, usuario.getTelefono());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getRol());
            ps.setString(5, usuario.getEstado());
            ps.setInt(6, usuario.getIdUsuario());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                con.commit();
                mostrarMensajeExito("Usuario actualizado exitosamente");
                return true;
            }
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            manejarError("Error al actualizar usuario", e);
        } finally {
            cerrarRecursos(ps, con);
        }

        return false;
    }

    public boolean cambiarContrasena(int idUsuario, String contrasenaActual, String contrasenaNueva) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_CambiarContrasena(?, ?, ?, ?, ?)}");

            cs.setInt(1, idUsuario);
            cs.setString(2, contrasenaActual);
            cs.setString(3, contrasenaNueva);
            cs.registerOutParameter(4, Types.BIT);
            cs.registerOutParameter(5, Types.BIT);

            cs.execute();

            boolean exito = cs.getBoolean(4);
            boolean incorrecta = cs.getBoolean(5);

            if (exito) {
                con.commit();
                mostrarMensajeExito("Contrasena cambiada exitosamente");
                return true;
            } else if (incorrecta) {
                mostrarMensajeError("Contrasena actual incorrecta");
            }
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            manejarError("Error al cambiar contrasena", e);
        } finally {
            cerrarRecursos(cs, con);
        }

        return false;
    }

    public boolean desbloquearUsuario(int idUsuarioAdmin, int idUsuarioBloqueado) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_DesbloquearUsuario(?, ?, ?, ?)}");

            cs.setInt(1, idUsuarioAdmin);
            cs.setInt(2, idUsuarioBloqueado);
            cs.registerOutParameter(3, Types.BIT);
            cs.registerOutParameter(4, Types.BIT);

            cs.execute();

            boolean desbloqueado = cs.getBoolean(3);
            boolean sinPermisos = cs.getBoolean(4);

            if (desbloqueado) {
                con.commit();
                mostrarMensajeExito("Usuario desbloqueado exitosamente");
                return true;
            } else if (sinPermisos) {
                mostrarMensajeError("No tiene permisos para desbloquear usuarios");
            }
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            manejarError("Error al desbloquear usuario", e);
        } finally {
            cerrarRecursos(cs, con);
        }

        return false;
    }

    public boolean verificarPermiso(int idUsuario, String permiso) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_VerificarPermiso(?, ?, ?)}");

            cs.setInt(1, idUsuario);
            cs.setString(2, permiso);
            cs.registerOutParameter(3, Types.BIT);

            cs.execute();

            return cs.getBoolean(3);
        } catch (SQLException e) {
            manejarError("Error al verificar permiso", e);
        } finally {
            cerrarRecursos(cs, con);
        }

        return false;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery("SELECT * FROM Usuario ORDER BY fechaCreacion DESC");

            while (rs.next()) {
                lista.add(parsear(rs));
            }
        } catch (SQLException e) {
            manejarError("Error al listar usuarios", e);
        } finally {
            cerrarRecursos(rs, st, con);
        }

        return lista;
    }

    public boolean existeUsuario(String nombreUsuario) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM Usuario WHERE nombreUsuario = ?");
            ps.setString(1, nombreUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            manejarError("Error al verificar existencia de usuario", e);
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return false;
    }

    public boolean existeEmail(String email) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM Usuario WHERE email = ?");
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            manejarError("Error al verificar existencia de email", e);
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return false;
    }

    private void cerrarRecursos(AutoCloseable... recursos) {
        for (AutoCloseable recurso : recursos) {
            if (recurso != null) {
                try {
                    recurso.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar recurso: " + e.getMessage());
                }
            }
        }
    }
}
