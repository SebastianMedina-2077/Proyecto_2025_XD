package Factory;
import Builder.ProductoBuilder;
import Catalogos.Estado;
import Clases.Cliente;
import Clases.Persona;
import Clases.Proveedor;
import Clases.Usuario;
import Clases_Tienda.Almacen;
import Clases_Tienda.Categoria;
import Clases_Tienda.Marca;
import Clases_Tienda.Producto;
import Clases_Tienda.VentaCabecera;
import Clases_Tienda.VentaDetalle;
import Enum.MetodoPago;
import Clases_Tienda.Inventario;
import Catalogos.Tipo;
import Catalogos.Estado;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EntidadFactory {
    // Factory para Productos
    public static Producto crearProductoBasico(String codigo, String nombre,
                                               BigDecimal precioCompra, BigDecimal precioVenta,
                                               Categoria categoria, Marca marca, Proveedor proveedor) {
        return new ProductoBuilder()
                .codigo(codigo)
                .nombre(nombre)
                .precioCompra(precioCompra)
                .precioVenta(precioVenta)
                .categoria(categoria)
                .marca(marca)
                .proveedor(proveedor)
                .stockMinimo(5)
                .estado(Estado.ACTIVO())
                .construir();
    }

    public static Producto crearProductoConVencimiento(String codigo, String nombre,
                                                       BigDecimal precioCompra, BigDecimal precioVenta,
                                                       Categoria categoria, Marca marca, Proveedor proveedor,
                                                       LocalDate fechaVencimiento, String lote, int stockMinimo) {
        return new ProductoBuilder()
                .codigo(codigo)
                .nombre(nombre)
                .precioCompra(precioCompra)
                .precioVenta(precioVenta)
                .categoria(categoria)
                .marca(marca)
                .proveedor(proveedor)
                .fechaVencimiento(fechaVencimiento)
                .lote(lote)
                .stockMinimo(stockMinimo)
                .estado(Estado.ACTIVO())
                .construir();
    }

    // Factory para Usuarios
    public static Usuario crearUsuario(String nombreUsuario, String contrasena, String dni,
                                       String nombre, String apellido, Tipo tipo) {
        return new Usuario(nombreUsuario, contrasena, dni, nombre, apellido, tipo);
    }

    public static Usuario crearAdministrador(String nombreUsuario, String contrasena,
                                             String dni, String nombre, String apellido) {
        return crearUsuario(nombreUsuario, contrasena, dni, nombre, apellido, Tipo.ADMINISTRADOR());
    }

    public static Usuario crearVendedor(String nombreUsuario, String contrasena,
                                        String dni, String nombre, String apellido) {
        return crearUsuario(nombreUsuario, contrasena, dni, nombre, apellido, Tipo.VENDEDOR());
    }

    // Factory para Clientes
    public static Cliente crearClienteRegular(String dni, String nombre, String apellido) {
        return Cliente.crearRegular(dni, nombre, apellido, null);
    }

    public static Cliente crearClienteMayorista(String dni, String nombre, String apellido,
                                                BigDecimal descuento) {
        return Cliente.crearMayorista(dni, nombre, apellido, descuento);
    }

    // Factory para Inventario
    public static Inventario crearInventario(Producto producto, Almacen almacen, int stockInicial, Usuario usuario) {
        return new Inventario(producto, almacen, stockInicial, usuario);
    }

    public static Inventario crearInventarioConReserva(Producto producto, Almacen almacen,
                                                       int stockInicial, int stockReservado, Usuario usuario) {
        Inventario inventario = new Inventario(producto, almacen, stockInicial, usuario);
        inventario.setStockReservado(stockReservado);
        return inventario;
    }

    // Factory para Ventas
    public static VentaCabecera crearVentaSimple(Cliente cliente, Usuario vendedor, MetodoPago metodoPago) {
        return new VentaCabecera(cliente, vendedor, metodoPago);
    }

    public static VentaCabecera crearVentaCompleta(Cliente cliente, Usuario vendedor, MetodoPago metodoPago,
                                                   List<VentaDetalle> detalles) {
        VentaCabecera venta = new VentaCabecera(cliente, vendedor, metodoPago);
        venta.agregarDetalles(detalles);
        return venta;
    }

    // Factory para Proveedores
    public static Proveedor crearProveedor(String razonSocial, String ruc, Persona contacto) {
        return new Proveedor(contacto, razonSocial, ruc);
    }

    public static Proveedor crearProveedorCompleto(String razonSocial, String ruc, Persona contacto,
                                                   String banco, String numeroCuenta) {
        Proveedor proveedor = crearProveedor(razonSocial, ruc, contacto);
        proveedor.actualizarDatosBancarios(banco, numeroCuenta);
        return proveedor;
    }
}
