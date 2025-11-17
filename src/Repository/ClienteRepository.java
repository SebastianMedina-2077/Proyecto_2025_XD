package Repository;

import DB_Conection.DAO;
import Models.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository extends DAO<Cliente> implements IClienteRepository {
    @Override
    public Cliente parsear(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(rs.getInt("idCliente"));
        cliente.setTipoCliente(rs.getString("tipoCliente"));
        cliente.setRazonSocial(rs.getString("razonSocial"));
        cliente.setDocumento(rs.getString("documento"));
        cliente.setTipoDocumento(rs.getString("tipoDocumento"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setEmail(rs.getString("email"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setDescuentoPersonalizado(rs.getDouble("descuentoPersonalizado"));

        Date fechaReg = rs.getDate("fechaRegistro");
        cliente.setFechaRegistro(fechaReg != null ? fechaReg.toLocalDate() : null);

        cliente.setEstado(rs.getString("estado"));
        return cliente;
    }

    @Override
    public List<Cliente> listarTodos() {
        return listarConFiltros(null, null);
    }

    @Override
    public Cliente obtenerPorId(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT * FROM Cliente WHERE idCliente = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener cliente", e);
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return null;
    }

    @Override
    public boolean insertar(Cliente cliente) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_InsertarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setString(1, cliente.getTipoCliente());
            cs.setString(2, cliente.getRazonSocial());
            cs.setString(3, cliente.getDocumento());
            cs.setString(4, cliente.getTipoDocumento());
            cs.setString(5, cliente.getTelefono());
            cs.setString(6, cliente.getEmail());
            cs.setString(7, cliente.getDireccion());
            cs.setDouble(8, cliente.getDescuentoPersonalizado());
            cs.registerOutParameter(9, Types.INTEGER);

            cs.execute();

            int idGenerado = cs.getInt(9);
            cliente.setIdCliente(idGenerado);

            con.commit();
            mostrarMensajeExito("Cliente registrado exitosamente");
            return true;

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            if (e.getMessage().contains("documento")) {
                mostrarMensajeError("El documento ya existe");
            } else {
                manejarError("Error al insertar cliente", e);
            }
        } finally {
            cerrarRecursos(cs, con);
        }

        return false;
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_ActualizarCliente(?, ?, ?, ?, ?, ?, ?)}");

            cs.setInt(1, cliente.getIdCliente());
            cs.setString(2, cliente.getTipoCliente());
            cs.setString(3, cliente.getRazonSocial());
            cs.setString(4, cliente.getTelefono());
            cs.setString(5, cliente.getEmail());
            cs.setString(6, cliente.getDireccion());
            cs.setDouble(7, cliente.getDescuentoPersonalizado());

            cs.execute();
            con.commit();

            mostrarMensajeExito("Cliente actualizado exitosamente");
            return true;

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            manejarError("Error al actualizar cliente", e);
        } finally {
            cerrarRecursos(cs, con);
        }

        return false;
    }

    @Override
    public boolean eliminar(int id) {
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "UPDATE Cliente SET estado = 'Inactivo' WHERE idCliente = ?");
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                con.commit();
                mostrarMensajeExito("Cliente eliminado exitosamente");
                return true;
            }

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            manejarError("Error al eliminar cliente", e);
        } finally {
            cerrarRecursos(ps, con);
        }

        return false;
    }

    public List<Cliente> listarConFiltros(String filtro, String tipoCliente) {
        List<Cliente> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            cs = con.prepareCall("{CALL SP_ListarClientes(?, ?)}");

            if (filtro == null || filtro.trim().isEmpty()) {
                cs.setNull(1, Types.VARCHAR);
            } else {
                cs.setString(1, filtro);
            }

            if (tipoCliente == null || tipoCliente.trim().isEmpty()) {
                cs.setNull(2, Types.VARCHAR);
            } else {
                cs.setString(2, tipoCliente);
            }

            rs = cs.executeQuery();

            while (rs.next()) {
                lista.add(parsear(rs));
            }

        } catch (SQLException e) {
            manejarError("Error al listar clientes", e);
        } finally {
            cerrarRecursos(rs, cs, con);
        }

        return lista;
    }

    public Cliente obtenerPorDocumento(String documento) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT * FROM Cliente WHERE documento = ? AND estado = 'Activo'");
            ps.setString(1, documento);
            rs = ps.executeQuery();

            if (rs.next()) {
                return parsear(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al obtener cliente por documento", e);
        } finally {
            cerrarRecursos(rs, ps, con);
        }

        return null;
    }

    public boolean existeDocumento(String documento) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            ps = con.prepareStatement(
                    "SELECT COUNT(*) FROM Cliente WHERE documento = ?");
            ps.setString(1, documento);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            manejarError("Error al verificar documento", e);
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
