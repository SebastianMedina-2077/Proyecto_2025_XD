package Clases_DAO;

import Clases.Cliente;
import DB_Conection.DAOAdaptado;
import Catalogos.Estado;
import Clases.Contacto;
import Enum.*;

import java.sql.*;
import java.util.List;

public class ClienteDAO extends DAOAdaptado<Cliente, Integer> {
    @Override
    protected String obtenerProcedureListarTodos() {
        return "sp_ListarClientes";
    }

    @Override
    protected String obtenerProcedureListarPorId() {
        return "sp_ListarClientePorId";
    }

    @Override
    protected String obtenerProcedureGuardar() {
        return "sp_GuardarCliente";
    }

    @Override
    protected String obtenerProcedureEliminar() {
        return "sp_EliminarCliente";
    }

    @Override
    protected String obtenerProcedureActualizar() {
        return "sp_ActualizarCliente";
    }

    @Override
    protected String obtenerProcedureBuscarPorCriterio() {
        return "sp_BuscarClientePorCriterio";
    }

    @Override
    protected List<Cliente> ejecutarProcedureListar(String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Cliente>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                // No parameters needed for list all
            }

            @Override
            public Cliente parsear(ResultSet rs) throws SQLException {
                return parsearCliente(rs);
            }
        });
    }

    @Override
    protected List<Cliente> ejecutarProcedureListarPorId(Integer id, String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Cliente>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setInt(1, id);
            }

            @Override
            public Cliente parsear(ResultSet rs) throws SQLException {
                return parsearCliente(rs);
            }
        });
    }

    @Override
    protected List<Cliente> ejecutarProcedureBuscarPorCriterio(String criterio, Object valor, String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Cliente>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, criterio);
                cs.setString(2, valor.toString());
            }

            @Override
            public Cliente parsear(ResultSet rs) throws SQLException {
                return parsearCliente(rs);
            }
        });
    }

    @Override
    protected Cliente ejecutarProcedureGuardar(Connection con, String procedure, Cliente cliente) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?, ?, ?, ?, ?, ?")) {
            cs.setString(1, cliente.getDni());
            cs.setString(2, cliente.getNombre());
            cs.setString(3, cliente.getApellido());
            cs.setString(4, cliente.getTipoCliente());
            cs.setBigDecimal(5, cliente.getDescuentoEspecial());

            // Datos de contacto
            String telefono = (cliente.getContacto() != null) ? cliente.getContacto().getNumeroTel() : null;
            String email = (cliente.getContacto() != null) ? cliente.getContacto().getCorreo() : null;

            cs.setString(6, telefono);
            cs.setString(7, email);

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return parsearCliente(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al guardar cliente", e);
        }
        return null;
    }

    @Override
    protected boolean ejecutarProcedureEliminar(Connection con, String procedure, Integer id) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?")) {
            cs.setInt(1, id);
            cs.execute();
            return true;
        } catch (SQLException e) {
            manejarError("Error al eliminar cliente", e);
            return false;
        }
    }

    @Override
    protected boolean ejecutarProcedureActualizar(Connection con, String procedure, Cliente cliente) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?, ?, ?, ?, ?, ?, ?")) {
            cs.setInt(1, cliente.getId());
            cs.setString(2, cliente.getDni());
            cs.setString(3, cliente.getNombre());
            cs.setString(4, cliente.getApellido());
            cs.setString(5, cliente.getTipoCliente());
            cs.setBigDecimal(6, cliente.getDescuentoEspecial());

            // Datos de contacto
            String telefono = (cliente.getContacto() != null) ? cliente.getContacto().getNumeroTel() : null;
            String email = (cliente.getContacto() != null) ? cliente.getContacto().getCorreo() : null;

            cs.setString(7, telefono);
            cs.setString(8, email);

            cs.execute();
            return true;
        } catch (SQLException e) {
            manejarError("Error al actualizar cliente", e);
            return false;
        }
    }

    @Override
    protected boolean validarEntidad(Cliente cliente) {
        if (!validarObjetoNulo(cliente, "Cliente")) {
            return false;
        }

        // Validaciones usando el sistema existente
        if (!ValidarCampoVacio(cliente.getNombre(), "Nombre")) {
            return false;
        }

        if (!ValidarCampoVacio(cliente.getApellido(), "Apellido")) {
            return false;
        }

        if (!ValidarCampoVacio(cliente.getDni(), "DNI")) {
            return false;
        }

        // Validaciones específicas para clientes
        if (cliente.getDni() != null && !cliente.getDni().matches("^[0-9]{8}$")) {
            mostrarMensajeError("El DNI debe tener exactamente 8 dígitos");
            return false;
        }

        // Validar email del contacto si existe
        if (cliente.getContacto() != null && cliente.getContacto().getCorreo() != null) {
            if (!cliente.getContacto().getCorreo().matches("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {
                mostrarMensajeError("El formato del email es incorrecto");
                return false;
            }
        }

        // Validar teléfono del contacto si existe
        if (cliente.getContacto() != null && cliente.getContacto().getNumeroTel() != null) {
            if (!cliente.getContacto().getNumeroTel().matches("^[0-9]{9}$")) {
                mostrarMensajeError("El teléfono debe tener exactamente 9 dígitos");
                return false;
            }
        }

        if (cliente.getFechaNacimiento() != null) {
            if (cliente.getFechaNacimiento().isAfter(java.time.LocalDate.now())) {
                mostrarMensajeError("La fecha de nacimiento no puede ser futura");
                return false;
            }

            // Verificar que sea mayor de 18 años
            if (cliente.getFechaNacimiento().isAfter(java.time.LocalDate.now().minusYears(18))) {
                mostrarMensajeAdvertencia("El cliente es menor de edad");
            }
        }

        if (cliente.getDescuentoEspecial() != null &&
                (cliente.getDescuentoEspecial().compareTo(java.math.BigDecimal.ZERO) < 0 ||
                        cliente.getDescuentoEspecial().compareTo(java.math.BigDecimal.valueOf(100)) > 0)) {
            mostrarMensajeError("El descuento especial debe estar entre 0% y 100%");
            return false;
        }

        return true;
    }

    private Cliente parsearCliente(ResultSet rs) throws SQLException {
        try {
            Cliente cliente = new Cliente();
            cliente.setId(rs.getInt("id"));
            cliente.setIdCliente(rs.getString("id_cliente"));
            cliente.setCodigoCliente(rs.getString("codigo_cliente"));
            cliente.setDni(rs.getString("dni"));
            cliente.setNombre(rs.getString("nombre"));
            cliente.setApellido(rs.getString("apellido"));
            cliente.setTipoCliente(rs.getString("tipo_cliente"));
            cliente.setDescuentoEspecial(rs.getBigDecimal("descuento_especial"));

            // Fechas
            cliente.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());

            Timestamp fechaMod = rs.getTimestamp("fecha_modificacion");
            if (fechaMod != null) {
                cliente.setFechaModificacion(fechaMod.toLocalDateTime());
            }

            // Fecha de nacimiento
            Date fechaNac = rs.getDate("fecha_nacimiento");
            if (fechaNac != null) {
                cliente.setFechaNacimiento(fechaNac.toLocalDate());
            }

            // Género
            String generoStr = rs.getString("genero");
            if (generoStr != null) {
                cliente.setGenero(Genero.valueOf(generoStr));
            }

            // Contacto
            Contacto contacto = new Contacto();
            contacto.setNumeroTel(rs.getString("telefono"));
            contacto.setCorreo(rs.getString("email"));
            cliente.setContacto(contacto);

            return cliente;
        } catch (SQLException e) {
            manejarError("Error al parsear cliente", e);
            return null;
        }
    }

    // Métodos de negocio específicos para Cliente
    public List<Cliente> buscarPorDNI(String dni) {
        return ejecutarProcedureGenerico("sp_BuscarClientePorDNI", new PreparedStatementCallback<Cliente>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, dni);
            }

            @Override
            public Cliente parsear(ResultSet rs) throws SQLException {
                return parsearCliente(rs);
            }
        });
    }

    public List<Cliente> buscarPorRUC(String ruc) {
        return ejecutarProcedureGenerico("sp_BuscarClientePorRUC", new PreparedStatementCallback<Cliente>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, ruc);
            }

            @Override
            public Cliente parsear(ResultSet rs) throws SQLException {
                return parsearCliente(rs);
            }
        });
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return ejecutarProcedureGenerico("sp_BuscarClientePorNombre", new PreparedStatementCallback<Cliente>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, "%" + nombre + "%");
            }

            @Override
            public Cliente parsear(ResultSet rs) throws SQLException {
                return parsearCliente(rs);
            }
        });
    }

    public boolean aplicarDescuentoEspecial(Integer clienteId, java.math.BigDecimal descuento) {
        try (Connection con = obtenerConexion()) {
            if (con == null) return false;

            String procedure = "sp_AplicarDescuentoCliente";
            try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?")) {
                cs.setInt(1, clienteId);
                cs.setBigDecimal(2, descuento);
                cs.execute();
                return true;
            }
        } catch (SQLException e) {
            manejarError("Error al aplicar descuento especial", e);
            return false;
        }
    }
}
