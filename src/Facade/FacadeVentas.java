package Facade;

import Clases.Cliente;
import Clases.Usuario;
import Clases_Tienda.*;
import Interface.InventarioDAO;
import Interface.ProductoDAO;
import Interface.UsuarioDAO;
import Interface.VentaDAO;
import Singleton.ConfiguracionSistema;
import Enum.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FacadeVentas {
    private ProductoDAO productoDAO;
    private InventarioDAO inventarioDAO;
    private VentaDAO ventaDAO;
    private UsuarioDAO usuarioDAO;
    private ConfiguracionSistema config;

    public FacadeVentas() {
        this.config = ConfiguracionSistema.obtenerInstancia();
        // Inicializar DAOs (en implementación real sería con inyección de dependencias)
    }

    public VentaCabecera realizarVentaCompleta(String idCliente, Integer idVendedor,
                                               List<VentaDetalle> detalles, MetodoPago metodoPago) {
        try {
            // 1. Validar cliente
            Cliente cliente = buscarClientePorId(idCliente);
            if (cliente == null) {
                throw new IllegalArgumentException("Cliente no encontrado");
            }

            // 2. Validar vendedor
            Usuario vendedor = buscarUsuarioPorId(idVendedor);
            if (vendedor == null || !vendedor.esVendedor()) {
                throw new IllegalArgumentException("Vendedor no válido");
            }

            // 3. Crear venta
            VentaCabecera venta = new VentaCabecera(cliente, vendedor, metodoPago);

            // 4. Agregar detalles y validar stock
            for (VentaDetalle detalle : detalles) {
                Producto producto = detalle.getProducto();
                Almacen almacen = detalle.getAlmacen();

                // Validar stock
                Inventario inventario = inventarioDAO.buscarPorProductoYAlmacen(
                        producto.getCodigoProducto(), almacen.getCodigoAlmacen());

                if (inventario == null || !inventario.tieneStockDisponible(detalle.getCantidad())) {
                    throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre());
                }

                venta.agregarDetalle(detalle);
            }

            // 5. Completar venta
            if (venta.completarVenta()) {
                // 6. Actualizar inventario
                for (VentaDetalle detalle : detalles) {
                    Producto producto = detalle.getProducto();
                    Almacen almacen = detalle.getAlmacen();

                    inventarioDAO.actualizarStock(
                            producto.getCodigoProducto(),
                            almacen.getCodigoAlmacen(),
                            inventarioDAO.buscarPorProductoYAlmacen(producto.getCodigoProducto(), almacen.getCodigoAlmacen()).getStockActual() - detalle.getCantidad(),
                            idVendedor
                    );
                }

                return venta;
            } else {
                throw new IllegalStateException("Error al completar la venta");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error en el proceso de venta: " + e.getMessage(), e);
        }
    }

    private Cliente buscarClientePorId(String idCliente) {
        // Implementar búsqueda en DAO
        return null; // Placeholder
    }

    private Usuario buscarUsuarioPorId(Integer idUsuario) {
        // Implementar búsqueda en DAO
        return null; // Placeholder
    }

    public List<Producto> obtenerProductosConStockBajo() {
        return productoDAO.productosConStockBajo();
    }

    public List<Producto> obtenerProductosProximosVencer() {
        return productoDAO.productosProximosVencer(config.getDiasAnticipacionVencimiento());
    }

    public BigDecimal calcularTotalVentasDelDia() {
        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);
        return ventaDAO.calcularTotalVentasPorPeriodo(inicio, fin);
    }
}
