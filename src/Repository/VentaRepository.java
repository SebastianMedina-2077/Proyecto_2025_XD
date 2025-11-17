package Repository;

import DB_Conection.DAO;
import Models.Venta;
import Models.DetalleVenta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaRepository extends DAO<Venta>{
    @Override
    public Venta parsear(ResultSet rs) throws SQLException {
        Venta v = new Venta();
        v.setIdVenta(rs.getInt("idVenta"));
        v.setNumeroVenta(rs.getString("numeroVenta"));
        v.setIdCliente(rs.getInt("idCliente"));
        v.setIdUsuario(rs.getInt("idUsuario"));
        v.setIdMedioPago(rs.getInt("idMedioPago"));

        Timestamp fecha = rs.getTimestamp("fechaVenta");
        v.setFechaVenta(fecha != null ? fecha.toLocalDateTime() : null);

        v.setSubtotal(rs.getDouble("subtotal"));
        v.setDescuentoTotal(rs.getDouble("descuentoTotal"));
        v.setIgv(rs.getDouble("igv"));
        v.setTotal(rs.getDouble("total"));
        v.setModalidadVenta(rs.getString("modalidadVenta"));
        v.setObservaciones(rs.getString("observaciones"));
        v.setEstado(rs.getString("estado"));

        return v;
    }

    public boolean registrarVenta(Venta venta) {
        Connection con = null;
        CallableStatement csVenta = null;
        CallableStatement csDetalle = null;

        try {
            con = getConnection();
            con.setAutoCommit(false);

            // 1. Registrar venta
            csVenta = con.prepareCall("{CALL SP_RegistrarVenta(?, ?, ?, ?, ?, ?)}");
            csVenta.setInt(1, venta.getIdCliente());
            csVenta.setInt(2, venta.getIdUsuario());
            csVenta.setInt(3, venta.getIdMedioPago());
            csVenta.setString(4, venta.getModalidadVenta());
            csVenta.setString(5, venta.getObservaciones());
            csVenta.registerOutParameter(6, Types.VARCHAR);

            csVenta.execute();
            String numeroVenta = csVenta.getString(6);
            venta.setNumeroVenta(numeroVenta);

            // 2. Registrar detalles
            csDetalle = con.prepareCall("{CALL SP_RegistrarDetalleVenta(?, ?, ?, ?, ?, ?, ?, ?)}");

            for (DetalleVenta detalle : venta.getDetalles()) {
                csDetalle.setInt(1, venta.getIdVenta());
                csDetalle.setInt(2, detalle.getIdProducto());
                csDetalle.setInt(3, detalle.getIdAlmacen());
                csDetalle.setInt(4, detalle.getCantidad());
                csDetalle.setString(5, detalle.getModalidadVenta());
                csDetalle.setDouble(6, detalle.getDescuentoPromocion());
                csDetalle.setDouble(7, detalle.getDescuentoAdicional());

                if (detalle.getIdPromocionAplicada() != null) {
                    csDetalle.setInt(8, detalle.getIdPromocionAplicada());
                } else {
                    csDetalle.setNull(8, Types.INTEGER);
                }

                csDetalle.execute();
            }

            con.commit();
            mostrarMensajeExito("Venta registrada exitosamente: " + numeroVenta);
            return true;

        } catch (SQLException e) {
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            manejarError("Error al registrar venta", e);
        } finally {
            cerrarRecursos(csDetalle, csVenta, con);
        }

        return false;
    }

    private void cerrarRecursos(AutoCloseable... recursos) {
        for (AutoCloseable recurso : recursos) {
            if (recurso != null) {
                try {
                    recurso.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar: " + e.getMessage());
                }
            }
        }
    }
}
