package Clases_DAO;

import Clases_Tienda.Producto;
import DB_Conection.DAOAdaptado;
import java.math.BigDecimal;
import Catalogos.Estado;
import Clases_Tienda.Categoria;
import Clases_Tienda.Marca;

import java.sql.*;
import java.util.List;

public class ProductoDAO extends DAOAdaptado<Producto, Integer> {
    @Override
    protected String obtenerProcedureListarTodos() {
        return "sp_ListarProductos";
    }

    @Override
    protected String obtenerProcedureBuscarPorCriterio() {
        return "sp_BuscarProductoPorCriterio";
    }

    @Override
    protected String obtenerProcedureListarPorId() {
        return "sp_ListarProductoPorId";
    }

    @Override
    protected String obtenerProcedureGuardar() {
        return "sp_GuardarProducto";
    }

    @Override
    protected String obtenerProcedureEliminar() {
        return "sp_EliminarProducto";
    }

    @Override
    protected String obtenerProcedureActualizar() {
        return "sp_ActualizarProducto";
    }

    @Override
    protected List<Producto> ejecutarProcedureListar(String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                // No parameters needed for list all
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    @Override
    protected List<Producto> ejecutarProcedureListarPorId(Integer id, String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setInt(1, id);
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    @Override
    protected List<Producto> ejecutarProcedureBuscarPorCriterio(String criterio, Object valor, String procedure) {
        return ejecutarProcedureGenerico(procedure, new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setString(1, criterio);
                cs.setString(2, valor.toString());
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    @Override
    protected Producto ejecutarProcedureGuardar(Connection con, String procedure, Producto producto) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?")) {
            cs.setString(1, producto.getCodigoProducto());
            cs.setString(2, producto.getNombre());
            cs.setString(3, producto.getDescripcion());
            cs.setBigDecimal(4, producto.getPrecioCompra());
            cs.setBigDecimal(5, producto.getPrecioVenta());
            cs.setInt(6, producto.getStockMinimo());
            cs.setString(7, producto.getLote());

            // Manejar fecha de vencimiento (puede ser null)
            if (producto.getFechaVencimiento() != null) {
                cs.setDate(8, java.sql.Date.valueOf(producto.getFechaVencimiento()));
            } else {
                cs.setNull(8, Types.DATE);
            }

            cs.setString(9, producto.getImagenPath());
            cs.setInt(10, producto.getCategoria().getId());
            cs.setInt(11, producto.getMarca().getId());
            cs.setInt(12, producto.getProveedor().getId());

            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                return parsearProducto(rs);
            }
        } catch (SQLException e) {
            manejarError("Error al guardar producto", e);
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
            manejarError("Error al eliminar producto", e);
            return false;
        }
    }

    @Override
    protected boolean ejecutarProcedureActualizar(Connection con, String procedure, Producto producto) {
        try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?")) {
            cs.setInt(1, producto.getId());
            cs.setString(2, producto.getCodigoProducto());
            cs.setString(3, producto.getNombre());
            cs.setString(4, producto.getDescripcion());
            cs.setBigDecimal(5, producto.getPrecioCompra());
            cs.setBigDecimal(6, producto.getPrecioVenta());
            cs.setInt(7, producto.getStockMinimo());
            cs.setString(8, producto.getLote());

            // Manejar fecha de vencimiento (puede ser null)
            if (producto.getFechaVencimiento() != null) {
                cs.setDate(9, java.sql.Date.valueOf(producto.getFechaVencimiento()));
            } else {
                cs.setNull(9, Types.DATE);
            }

            cs.setString(10, producto.getImagenPath());
            cs.setInt(11, producto.getCategoria().getId());
            cs.setInt(12, producto.getMarca().getId());
            cs.setInt(13, producto.getProveedor().getId());

            cs.execute();
            return true;
        } catch (SQLException e) {
            manejarError("Error al actualizar producto", e);
            return false;
        }
    }

    @Override
    protected boolean validarEntidad(Producto producto) {
        if (!validarObjetoNulo(producto, "Producto")) {
            return false;
        }

        // Validaciones usando el sistema existente
        if (!ValidarCampoVacio(producto.getCodigoProducto(), "Código")) {
            return false;
        }

        if (!ValidarCampoVacio(producto.getNombre(), "Nombre")) {
            return false;
        }

        // Validaciones de negocio específicas para productos
        if (producto.getPrecioCompra() == null || producto.getPrecioCompra().compareTo(BigDecimal.ZERO) <= 0) {
            mostrarMensajeError("El precio de compra debe ser mayor a cero");
            return false;
        }

        if (producto.getPrecioVenta() == null || producto.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
            mostrarMensajeError("El precio de venta debe ser mayor a cero");
            return false;
        }

        if (producto.getPrecioVenta().compareTo(producto.getPrecioCompra()) <= 0) {
            mostrarMensajeError("El precio de venta debe ser mayor al precio de compra");
            return false;
        }

        if (producto.getStockMinimo() < 0) {
            mostrarMensajeError("El stock mínimo no puede ser negativo");
            return false;
        }

        if (producto.getCategoria() == null) {
            mostrarMensajeError("Debe seleccionar una categoría");
            return false;
        }

        if (producto.getMarca() == null) {
            mostrarMensajeError("Debe seleccionar una marca");
            return false;
        }

        if (producto.getEstado() == null) {
            mostrarMensajeError("Debe seleccionar un estado");
            return false;
        }

        if (producto.getProveedor() == null) {
            mostrarMensajeError("Debe seleccionar un proveedor");
            return false;
        }
        return true;
    }

    private Producto parsearProducto(ResultSet rs) throws SQLException {
        try {
            Producto producto = new Producto();
            producto.setId(rs.getInt("id"));
            producto.setCodigoProducto(rs.getString("codigo"));
            producto.setNombre(rs.getString("nombre"));
            producto.setDescripcion(rs.getString("descripcion"));
            producto.setPrecioCompra(rs.getBigDecimal("precio_compra"));
            producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
            producto.setStockMinimo(rs.getInt("stock_minimo"));
            producto.setLote(rs.getString("lote"));

            // Manejar fecha de vencimiento (puede ser null)
            Date fechaVenc = rs.getDate("fecha_vencimiento");
            if (fechaVenc != null) {
                producto.setFechaVencimiento(fechaVenc.toLocalDate());
            }

            producto.setImagenPath(rs.getString("imagen_path"));
            producto.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());

            // Manejar fecha_modificacion que puede ser null
            Timestamp fechaMod = rs.getTimestamp("fecha_modificacion");
            if (fechaMod != null) {
                producto.setFechaModificacion(fechaMod.toLocalDateTime());
            }

            // Categoría
            Categoria categoria = new Categoria();
            categoria.setId(rs.getInt("categoria_id"));
            categoria.setNombreCategoria(rs.getString("categoria_nombre"));
            producto.setCategoria(categoria);

            // Marca
            Marca marca = new Marca();
            marca.setId(rs.getInt("marca_id"));
            marca.setNombreMarca(rs.getString("marca_nombre"));
            producto.setMarca(marca);

            // Estado
            Estado estado = new Estado();
            estado.setId(rs.getInt("estado_id"));
            estado.setDescripcion(rs.getString("estado_nombre"));
            producto.setEstado(estado);

            return producto;
        } catch (SQLException e) {
            manejarError("Error al parsear producto", e);
            return null;
        }
    }

    // Métodos de negocio específicos para Producto
    public List<Producto> buscarPorCategoria(Integer categoriaId) {
        return ejecutarProcedureGenerico("sp_BuscarProductoPorCategoria", new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setInt(1, categoriaId);
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    public List<Producto> buscarPorMarca(Integer marcaId) {
        return ejecutarProcedureGenerico("sp_BuscarProductoPorMarca", new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setInt(1, marcaId);
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    public List<Producto> buscarPorPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return ejecutarProcedureGenerico("sp_BuscarProductoPorPrecio", new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setBigDecimal(1, precioMin);
                cs.setBigDecimal(2, precioMax);
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    public List<Producto> buscarPorStockBajo(Integer almacenId) {
        return ejecutarProcedureGenerico("sp_BuscarProductoStockBajo", new PreparedStatementCallback<Producto>() {
            @Override
            public void setParameters(CallableStatement cs) throws SQLException {
                cs.setInt(1, almacenId);
            }

            @Override
            public Producto parsear(ResultSet rs) throws SQLException {
                return parsearProducto(rs);
            }
        });
    }

    public boolean actualizarPrecio(Integer productoId, BigDecimal nuevoPrecioVenta) {
        try (Connection con = obtenerConexion()) {
            if (con == null) return false;

            String procedure = "sp_ActualizarPrecioProducto";
            try (CallableStatement cs = con.prepareCall("EXEC " + procedure + " ?, ?")) {
                cs.setInt(1, productoId);
                cs.setBigDecimal(2, nuevoPrecioVenta);
                cs.execute();
                return true;
            }
        } catch (SQLException e) {
            manejarError("Error al actualizar precio", e);
            return false;
        }
    }
}
