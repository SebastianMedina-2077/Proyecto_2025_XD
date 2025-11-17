package Repository;

import DB_Conection.DAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAO extends DAO<Object>{
    @Override
    public Object parsear(ResultSet rs) throws SQLException {
        return null;
    }

    public Map<String, Double> obtenerVentasPorCategoria() {
        Map<String, Double> datos = new HashMap<>();

        try (Connection con = getConnection();
             CallableStatement cs = con.prepareCall(
                     "{CALL SP_VentasPorCategoria(?)}")) {

            cs.setDate(1, Date.valueOf(LocalDate.now().minusMonths(1)));
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {
                String categoria = rs.getString("categoria");
                double total = rs.getDouble("totalVendido");
                datos.put(categoria, total);
            }

        } catch (SQLException e) {
            manejarError("Error al obtener ventas por categoria", e);
        }

        return datos;
    }

    public Map<String, Double> obtenerVentasPorMes() {
        Map<String, Double> datos = new HashMap<>();

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            String query = "SELECT " +
                    "DATENAME(month, fechaVenta) as mes, " +
                    "SUM(total) as totalMes " +
                    "FROM Venta " +
                    "WHERE fechaVenta >= DATEADD(MONTH, -6, GETDATE()) " +
                    "AND estado = 'Completada' " +
                    "GROUP BY DATENAME(month, fechaVenta), MONTH(fechaVenta) " +
                    "ORDER BY MONTH(fechaVenta)";

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String mes = rs.getString("mes");
                double total = rs.getDouble("totalMes");
                datos.put(mes, total);
            }

        } catch (SQLException e) {
            manejarError("Error al obtener ventas por mes", e);
        }

        return datos;
    }

    public Map<String, Integer> obtenerProductosMasVendidos() {
        Map<String, Integer> datos = new HashMap<>();

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            String query = "SELECT TOP 10 " +
                    "p.nombreProducto, " +
                    "SUM(dv.cantidad) as cantidadVendida " +
                    "FROM DetalleVenta dv " +
                    "INNER JOIN Producto p ON dv.idProducto = p.idProducto " +
                    "INNER JOIN Venta v ON dv.idVenta = v.idVenta " +
                    "WHERE v.fechaVenta >= DATEADD(MONTH, -1, GETDATE()) " +
                    "AND v.estado = 'Completada' " +
                    "GROUP BY p.nombreProducto " +
                    "ORDER BY cantidadVendida DESC";

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String producto = rs.getString("nombreProducto");
                int cantidad = rs.getInt("cantidadVendida");
                datos.put(producto, cantidad);
            }

        } catch (SQLException e) {
            manejarError("Error al obtener productos mas vendidos", e);
        }

        return datos;
    }

    public Map<String, Double> obtenerVentasPorDia(int dias) {
        Map<String, Double> datos = new HashMap<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT " +
                             "CAST(fechaVenta AS DATE) as fecha, " +
                             "SUM(total) as totalDia " +
                             "FROM Venta " +
                             "WHERE fechaVenta >= DATEADD(DAY, -?, GETDATE()) " +
                             "AND estado = 'Completada' " +
                             "GROUP BY CAST(fechaVenta AS DATE) " +
                             "ORDER BY fecha")) {

            ps.setInt(1, dias);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Date fecha = rs.getDate("fecha");
                double total = rs.getDouble("totalDia");
                datos.put(fecha.toString(), total);
            }

        } catch (SQLException e) {
            manejarError("Error al obtener ventas por dia", e);
        }

        return datos;
    }

    public double obtenerTotalVentasMes() {
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            String query = "SELECT ISNULL(SUM(total), 0) as total " +
                    "FROM Venta " +
                    "WHERE MONTH(fechaVenta) = MONTH(GETDATE()) " +
                    "AND YEAR(fechaVenta) = YEAR(GETDATE()) " +
                    "AND estado = 'Completada'";

            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                return rs.getDouble("total");
            }

        } catch (SQLException e) {
            manejarError("Error al obtener total ventas mes", e);
        }

        return 0.0;
    }

    public int obtenerProductosBajoStock() {
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            String query = "SELECT COUNT(*) as cantidad " +
                    "FROM VW_StockConsolidado " +
                    "WHERE estadoStock IN ('CRITICO', 'SIN STOCK')";

            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                return rs.getInt("cantidad");
            }

        } catch (SQLException e) {
            manejarError("Error al obtener productos bajo stock", e);
        }

        return 0;
    }

    public int obtenerTotalClientes() {
        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            String query = "SELECT COUNT(*) as total " +
                    "FROM Cliente " +
                    "WHERE estado = 'Activo'";

            ResultSet rs = st.executeQuery(query);

            if (rs.next()) {
                return rs.getInt("total");
            }

        } catch (SQLException e) {
            manejarError("Error al obtener total clientes", e);
        }

        return 0;
    }

    public Map<String, Double> obtenerVentasPorTipoCliente() {
        Map<String, Double> datos = new HashMap<>();

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {

            String query = "SELECT " +
                    "c.tipoCliente, " +
                    "SUM(v.total) as totalVentas " +
                    "FROM Venta v " +
                    "INNER JOIN Cliente c ON v.idCliente = c.idCliente " +
                    "WHERE v.fechaVenta >= DATEADD(MONTH, -1, GETDATE()) " +
                    "AND v.estado = 'Completada' " +
                    "GROUP BY c.tipoCliente";

            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String tipo = rs.getString("tipoCliente");
                double total = rs.getDouble("totalVentas");
                datos.put(tipo, total);
            }

        } catch (SQLException e) {
            manejarError("Error al obtener ventas por tipo cliente", e);
        }

        return datos;
    }
}
