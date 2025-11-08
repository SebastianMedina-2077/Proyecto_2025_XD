package Clases_DAO;

import Catalogos.Estado;
import Catalogos.Tipo;
import Clases.Contacto;
import Clases.Usuario;
import DB_Conection.DAO;
import DB_Conection.DAOAdaptado;
import Clases.*;
import Enum.Genero;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

public class UsuarioDAO extends DAOAdaptado<Usuario, Integer> {
    @Override
    protected String obtenerProcedureListarTodos() {
        return "sp_ListarUsuarios";
    }

    @Override
    protected String obtenerProcedureListarPorId() {
        return "sp_ListarUsuarioPorId";
    }

    @Override
    protected String obtenerProcedureGuardar() {
        return "sp_RegistrarUsuario";
    }

    @Override
    protected String obtenerProcedureEliminar() {
        return "sp_EliminarUsuario";
    }

    @Override
    protected String obtenerProcedureActualizar() {
        return "sp_ActualizarUsuario";
    }

    @Override
    protected List<Usuario> ejecutarProcedureListar(String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Usuario>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                // No parameters needed for list all
            }

            @Override
            public Usuario parsear(ResultSet rs) throws SQLException {
                return parsearUsuario(rs);
            }
        });
    }

    @Override
    protected List<Usuario> ejecutarProcedureListarPorId(Integer id, String procedure) {
        return ejecutarProcedureGenericoConParametros(procedure, new PreparedStatementCallback<Usuario>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setInt(1, id);
            }

            @Override
            public Usuario parsear(ResultSet rs) throws SQLException {
                return parsearUsuario(rs);
            }
        }, 1); // <- 1 parámetro
    }

    @Override
    protected Usuario ejecutarProcedureGuardar(Connection con, String procedure, Usuario usuario) {
        // IMPORTANTE: Necesitamos el ID del administrador que está creando el usuario
        Integer idUsuarioAdmin = obtenerIdUsuarioActual();

        if (idUsuarioAdmin == null) {
            mostrarMensajeError("No se puede crear usuario: no hay un administrador autenticado");
            return null;
        }

        try (CallableStatement cs = con.prepareCall(
                "{CALL SP_RegistrarUsuario(?, ?, ?, ?, ?, ?, ?)}"
        )) {
            // Parámetros de entrada
            cs.setString(1, usuario.getNombreUsuario());
            cs.setString(2, usuario.getContrasena());
            cs.setString(3, usuario.getDni());
            cs.setInt(4, usuario.getTipo().getId());
            cs.setInt(5, idUsuarioAdmin);

            // Parámetros de salida
            cs.registerOutParameter(6, Types.INTEGER); // @Resultado
            cs.registerOutParameter(7, Types.VARCHAR); // @Mensaje

            cs.execute();

            int resultado = cs.getInt(6);
            String mensaje = cs.getString(7);

            if (resultado == 1) {
                mostrarMensajeExito(mensaje);
                // Recargar el usuario desde la BD para obtener el ID generado
                return obtenerUsuarioPorNombre(usuario.getNombreUsuario());
            } else {
                mostrarMensajeError(mensaje);
                return null;
            }

        } catch (SQLException e) {
            manejarError("Error al registrar usuario", e);
            return null;
        }
    }

    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        List<Usuario> usuarios = listarPorCriterio("NOMBRE_US", nombreUsuario);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    public Usuario obtenerUsuarioPorDni(String dni) {
        List<Usuario> usuarios = listarPorCriterio("DNI_SM", dni);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    @Override
    protected boolean ejecutarProcedureEliminar(Connection con, String procedure, Integer id) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?")) {
            cs.setInt(1, id);
            cs.execute();
            return true;
        } catch (SQLException e) {
            manejarError("Error al eliminar usuario", e);
            return false;
        }
    }

    @Override
    protected boolean ejecutarProcedureActualizar(Connection con, String procedure, Usuario usuario) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?")) {
            cs.setInt(1, usuario.getId());
            cs.setString(2, usuario.getDni());
            cs.setString(3, usuario.getNombre());
            cs.setString(4, usuario.getApellido());
            cs.setString(5, usuario.getNombreUsuario());
            cs.setString(6, usuario.getContrasena());
            cs.setInt(7, usuario.getEstado().getId());
            cs.setDate(8, java.sql.Date.valueOf(usuario.getFechaNacimiento()));

            String telefono = (usuario.getContacto() != null) ? usuario.getContacto().getNumeroTel() : null;
            String email = (usuario.getContacto() != null) ? usuario.getContacto().getCorreo() : null;

            cs.setString(9,telefono);
            cs.setString(10, email);
            cs.setInt(11, usuario.getTipo().getId());

            cs.execute();
            return true;
        } catch (SQLException e) {
            manejarError("Error al actualizar usuario", e);
            return false;
        }
    }

    @Override
    protected boolean validarEntidad(Usuario usuario) {
        // Validaciones usando el sistema existente
        if (!validarObjetoNulo(usuario, "Usuario")) {
            return false;
        }

        if (!ValidarCampoVacio(usuario.getDni(), "DNI")) {
            return false;
        }

        if (!ValidarCampoVacio(usuario.getNombre(), "Nombre")) {
            return false;
        }

        if (!ValidarCampoVacio(usuario.getApellido(), "Apellido")) {
            return false;
        }

        if (!ValidarCampoVacio(usuario.getNombreUsuario(), "Nombre de Usuario")) {
            return false;
        }

        // Validaciones específicas
        if (usuario.getDni() != null && !usuario.getDni().matches("^[0-9]{8}$")) {
            mostrarMensajeError("El DNI debe tener exactamente 8 dígitos");
            return false;
        }

        // Validar email si el contacto existe
        if (usuario.getContacto() != null && usuario.getContacto().getCorreo() != null) {
            if (!usuario.getContacto().getCorreo().matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
                mostrarMensajeError("El formato del email es incorrecto");
                return false;
            }
        }

        // Validar teléfono si el contacto existe
        if (usuario.getContacto() != null && usuario.getContacto().getNumeroTel() != null) {
            if (!usuario.getContacto().getNumeroTel().matches("^[0-9]{9}$")) {
                mostrarMensajeError("El teléfono debe tener exactamente 9 dígitos");
                return false;
            }
        }

        if (usuario.getContrasena() != null && usuario.getContrasena().length() < 6) {
            mostrarMensajeError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        return true;
    }

    private Usuario parsearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
        usuario.setNombreUsuario(rs.getString("NOMBRE_US"));
        usuario.setContrasena(rs.getString("CONTRASEÑA_US"));
        usuario.setDni(rs.getString("DNI_SM"));

        // Fechas
        Timestamp fechaReg = rs.getTimestamp("FECHA_REGISTRO_US");
        if (fechaReg != null) {
            usuario.setFechaRegistro(fechaReg.toLocalDateTime());
        }

        Timestamp ultimoIngreso = rs.getTimestamp("ULTIMO_INGRESO");
        if (ultimoIngreso != null) {
            usuario.setUltimoIngreso(ultimoIngreso.toLocalDateTime());
        }

        usuario.setIntentosFallidos(rs.getInt("INTENTOS_FALLIDOS"));

        // Estado
        Estado estado = new Estado();
        estado.setDescripcion(rs.getString("DESCRIP_ESTA"));
        estado.setActivo(rs.getBoolean("ACTIVO"));
        usuario.setEstado(estado);

        // Tipo
        Tipo tipo = new Tipo();
        tipo.setNombre(rs.getString("NOMBRE_TIP"));
        tipo.setCategoria(rs.getString("CATEGORIA_TIPO"));
        usuario.setTipo(tipo);

        // Datos de persona
        usuario.setNombre(rs.getString("NOMBRE_SM"));
        usuario.setApellido(rs.getString("APELLIDO_SM"));

        Date fechaNac = rs.getDate("FECHA_NACIMIENTO");
        if (fechaNac != null) {
            usuario.setFechaNacimiento(fechaNac.toLocalDate());
        }

        String generoStr = rs.getString("GENERO");
        if (generoStr != null && !generoStr.isEmpty()) {
            usuario.setGenero(generoStr.equals("M") ? Genero.M : Genero.F);
        }

        return usuario;
    }

    // Métodos de negocio específicos para Usuario
    public ResultadoLogin validarLogin(String nombreUsuario, String contrasena) {
        try (Connection con = obtenerConexion()) {
            if (con == null) {
                return new ResultadoLogin(false, "Error de conexión", null, null);
            }

            try (CallableStatement cs = con.prepareCall(
                    "{CALL SP_ValidarLogin(?, ?, ?, ?, ?, ?)}"
            )) {
                cs.setString(1, nombreUsuario);
                cs.setString(2, contrasena);

                cs.registerOutParameter(3, Types.INTEGER); // @Resultado
                cs.registerOutParameter(4, Types.VARCHAR); // @Mensaje
                cs.registerOutParameter(5, Types.INTEGER); // @IdUsuario
                cs.registerOutParameter(6, Types.VARCHAR); // @TipoUsuario

                cs.execute();

                int resultado = cs.getInt(3);
                String mensaje = cs.getString(4);

                if (resultado == 1) {
                    Integer idUsuario = cs.getInt(5);
                    String tipoUsuario = cs.getString(6);

                    // Cargar usuario completo
                    Usuario usuario = obtenerPorId(idUsuario);

                    return new ResultadoLogin(true, mensaje, usuario, tipoUsuario);
                } else {
                    return new ResultadoLogin(false, mensaje, null, null);
                }
            }
        } catch (SQLException e) {
            manejarError("Error en validación de login", e);
            return new ResultadoLogin(false, "Error al validar credenciales", null, null);
        }
    }

    public boolean bloquearUsuario(Integer usuarioId) {
        try (Connection con = obtenerConexion()) {
            if (con == null) return false;

            String procedure = "sp_BloquearUsuario";
            try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?")) {
                cs.setInt(1, usuarioId);
                cs.execute();
                return true;
            }
        } catch (SQLException e) {
            manejarError("Error al bloquear usuario", e);
            return false;
        }
    }

    public boolean desbloquearUsuario(Integer idUsuarioBloquear) {
        Integer idUsuarioAdmin = obtenerIdUsuarioActual();

        if (idUsuarioAdmin == null) {
            mostrarMensajeError("No hay un administrador autenticado");
            return false;
        }

        try (Connection con = obtenerConexion()) {
            if (con == null) return false;

            try (CallableStatement cs = con.prepareCall(
                    "{CALL SP_DesbloquearUsuario(?, ?, ?, ?)}"
            )) {
                cs.setInt(1, idUsuarioAdmin);
                cs.setInt(2, idUsuarioBloquear);
                cs.registerOutParameter(3, Types.INTEGER);
                cs.registerOutParameter(4, Types.VARCHAR);

                cs.execute();

                int resultado = cs.getInt(3);
                String mensaje = cs.getString(4);

                if (resultado == 1) {
                    mostrarMensajeExito(mensaje);
                    return true;
                } else {
                    mostrarMensajeError(mensaje);
                    return false;
                }
            }
        } catch (SQLException e) {
            manejarError("Error al desbloquear usuario", e);
            return false;
        }
    }

    private Integer obtenerIdUsuarioActual() {
        Usuario usuarioActual = Servicios.UsuarioServicio.getUsuarioActual();
        return usuarioActual != null ? usuarioActual.getIdUsuario() : null;
    }

    @Override
    protected String obtenerProcedureBuscarPorCriterio() {
        return "sp_BuscarUsuarioPorCriterio";
    }

    @Override
    protected List<Usuario> ejecutarProcedureBuscarPorCriterio(String criterio, Object valor, String procedure) {
        return ejecutarProcedureGenericoConParametros(procedure, new PreparedStatementCallback<Usuario>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, criterio);
                cs.setString(2, valor.toString());
            }

            @Override
            public Usuario parsear(ResultSet rs) throws SQLException {
                return parsearUsuario(rs);
            }
        }, 2); // <- 2 parámetros
    }
}
