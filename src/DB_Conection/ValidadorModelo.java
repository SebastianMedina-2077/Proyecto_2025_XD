package DB_Conection;

import Clases.*;
import Catalogos.*;
import Clases_Tienda.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidadorModelo {
    // Patrones de validación predefinidos
    private static final Pattern PATRON_DNI = Pattern.compile("^[0-9]{8}$");
    private static final Pattern PATRON_RUC = Pattern.compile("^[0-9]{11}$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^[0-9]{9}$");
    private static final Pattern PATRON_EMAIL = Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PATRON_CODIGO_BARRAS = Pattern.compile("^[0-9]{8,13}$");

    // Validaciones para Usuario
    public static boolean validarUsuario(Usuario usuario) {
        if (usuario == null) return false;

        return validarCampoRequerido(usuario.getDni(), "DNI") &&
                validarCampoRequerido(usuario.getNombre(), "Nombre") &&
                validarCampoRequerido(usuario.getApellido(), "Apellido") &&
                validarCampoRequerido(usuario.getNombreUsuario(), "Nombre de Usuario") &&
                validarDNI(usuario.getDni()) &&
                validarEmail(usuario.getContacto().getCorreo()) &&
                validarTelefono(usuario.getContacto().getNumeroTel()) &&
                validarContrasena(usuario.getContrasena()) &&
                validarFechaNacimiento(usuario.getFechaNacimiento());
    }

    // Validaciones para Cliente
    public static boolean validarCliente(Cliente cliente) {
        if (cliente == null) return false;

        return validarCampoRequerido(cliente.getNombre(), "Nombre") &&
                validarCampoRequerido(cliente.getApellido(), "Apellido") &&
                validarOpcionalDNI(cliente.getDni()) &&
                validarEmail(cliente.getContacto().getCorreo()) &&
                validarTelefono(cliente.getContacto().getNumeroTel()) &&
                validarFechaNacimiento(cliente.getFechaNacimiento()) &&
                validarDescuento(cliente.getDescuentoEspecial());
    }

    // Validaciones para Producto
    public static boolean validarProducto(Producto producto) {
        if (producto == null) return false;

        return validarCampoRequerido(producto.getCodigoProducto(), "Código") &&
                validarCampoRequerido(producto.getNombre(), "Nombre") &&
                validarPrecioCompra(producto.getPrecioCompra()) &&
                validarPrecioVenta(producto.getPrecioVenta()) &&
                validarRelacionPrecios(producto.getPrecioCompra(), producto.getPrecioVenta()) &&
                validarStockMinimo(producto.getStockMinimo()) &&
                validarCategoria(producto.getCategoria()) &&
                validarMarca(producto.getMarca()) &&
                validarEstado(producto.getEstado());
    }

    // Métodos de validación específicos
    private static boolean validarCampoRequerido(String campo, String nombreCampo) {
        return campo != null && !campo.trim().isEmpty();
    }

    private static boolean validarDNI(String dni) {
        return dni != null && PATRON_DNI.matcher(dni).matches();
    }

    private static boolean validarOpcionalDNI(String dni) {
        return dni == null || dni.trim().isEmpty() || PATRON_DNI.matcher(dni).matches();
    }

    private static boolean validarRUC(String ruc) {
        return ruc != null && PATRON_RUC.matcher(ruc).matches();
    }

    private static boolean validarOpcionalRUC(String ruc) {
        return ruc == null || ruc.trim().isEmpty() || PATRON_RUC.matcher(ruc).matches();
    }

    private static boolean validarEmail(String email) {
        return email == null || email.trim().isEmpty() || PATRON_EMAIL.matcher(email).matches();
    }

    private static boolean validarTelefono(String telefono) {
        return telefono == null || telefono.trim().isEmpty() || PATRON_TELEFONO.matcher(telefono).matches();
    }

    private static boolean validarContrasena(String contrasena) {
        return contrasena != null && contrasena.length() >= 6;
    }

    private static boolean validarFechaNacimiento(LocalDate fechaNacimiento) {
        return fechaNacimiento == null || !fechaNacimiento.isAfter(LocalDate.now());
    }

    private static boolean validarDescuento(BigDecimal descuento) {
        return descuento == null ||
                (descuento.compareTo(BigDecimal.ZERO) >= 0 &&
                        descuento.compareTo(BigDecimal.valueOf(100)) <= 0);
    }

    private static boolean validarPrecioCompra(BigDecimal precio) {
        return precio != null && precio.compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean validarPrecioVenta(BigDecimal precio) {
        return precio != null && precio.compareTo(BigDecimal.ZERO) > 0;
    }

    private static boolean validarRelacionPrecios(BigDecimal precioCompra, BigDecimal precioVenta) {
        return precioCompra == null || precioVenta == null ||
                precioVenta.compareTo(precioCompra) > 0;
    }

    private static boolean validarStockMinimo(int stockMinimo) {
        return stockMinimo >= 0;
    }

    private static boolean validarCategoria(Categoria categoria) {
        return categoria != null && categoria.getId() != null;
    }

    private static boolean validarMarca(Marca marca) {
        return marca != null && marca.getId() != null;
    }

    private static boolean validarEstado(Estado estado) {
        return estado != null && estado.getId() != null;
    }

    private static boolean validarCodigoBarrasOpcional(String codigoBarras) {
        return codigoBarras == null || codigoBarras.trim().isEmpty() ||
                PATRON_CODIGO_BARRAS.matcher(codigoBarras).matches();
    }

    // Métodos de utilidad adicionales
    public static String obtenerMensajeErrorUsuario(Usuario usuario) {
        if (usuario == null) return "El usuario no puede ser null";

        StringBuilder errores = new StringBuilder();

        if (!validarCampoRequerido(usuario.getDni(), "DNI")) {
            errores.append("El DNI es requerido\\n");
        } else if (!validarDNI(usuario.getDni())) {
            errores.append("El DNI debe tener 8 dígitos\\n");
        }

        if (!validarCampoRequerido(usuario.getNombre(), "Nombre")) {
            errores.append("El nombre es requerido\\n");
        }

        if (!validarCampoRequerido(usuario.getApellido(), "Apellido")) {
            errores.append("El apellido es requerido\\n");
        }

        if (!validarCampoRequerido(usuario.getNombreUsuario(), "Nombre de Usuario")) {
            errores.append("El nombre de usuario es requerido\\n");
        }

        if (!validarContrasena(usuario.getContrasena())) {
            errores.append("La contraseña debe tener al menos 6 caracteres\\n");
        }

        if (!validarEmail(usuario.getContacto().getCorreo())) {
            errores.append("El formato del email es incorrecto\\n");
        }

        return errores.toString();
    }

    public static String obtenerMensajeErrorProducto(Producto producto) {
        if (producto == null) return "El producto no puede ser null";

        StringBuilder errores = new StringBuilder();

        if (!validarCampoRequerido(producto.getCodigoProducto(), "Código")) {
            errores.append("El código es requerido\\n");
        }

        if (!validarCampoRequerido(producto.getNombre(), "Nombre")) {
            errores.append("El nombre es requerido\\n");
        }

        if (!validarPrecioCompra(producto.getPrecioCompra())) {
            errores.append("El precio de compra debe ser mayor a cero\\n");
        }

        if (!validarPrecioVenta(producto.getPrecioVenta())) {
            errores.append("El precio de venta debe ser mayor a cero\\n");
        }

        if (!validarRelacionPrecios(producto.getPrecioCompra(), producto.getPrecioVenta())) {
            errores.append("El precio de venta debe ser mayor al precio de compra\\n");
        }

        if (!validarCategoria(producto.getCategoria())) {
            errores.append("La categoría es requerida\\n");
        }

        if (!validarMarca(producto.getMarca())) {
            errores.append("La marca es requerida\\n");
        }

        if (!validarEstado(producto.getEstado())) {
            errores.append("El estado es requerido\\n");
        }

        return errores.toString();
    }

    // Método para validar todos los tipos de entidades
    public static boolean validarEntidad(Object entidad) {
        if (entidad == null) return false;

        if (entidad instanceof Usuario) {
            return validarUsuario((Usuario) entidad);
        } else if (entidad instanceof Cliente) {
            return validarCliente((Cliente) entidad);
        } else if (entidad instanceof Producto) {
            return validarProducto((Producto) entidad);
        }

        return false; // Tipo no reconocido
    }

    // Método para obtener mensaje de error
    public static String obtenerMensajeError(Object entidad) {
        if (entidad == null) return "La entidad no puede ser null";

        if (entidad instanceof Usuario) {
            return obtenerMensajeErrorUsuario((Usuario) entidad);
        } else if (entidad instanceof Cliente) {
            // Se puede implementar similar al de Usuario
            return "Errores de validación en Cliente";
        } else if (entidad instanceof Producto) {
            return obtenerMensajeErrorProducto((Producto) entidad);
        }

        return "Tipo de entidad no reconocido";
    }
}
