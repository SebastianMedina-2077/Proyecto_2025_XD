package Clases;

import java.math.BigDecimal;
import java.time.LocalDate;
import Enum.Genero;

public class Cliente extends Persona {
    private String idCliente;
    private String codigoCliente;
    private String tipoCliente;
    private BigDecimal descuentoEspecial;

    // Constructor por defecto
    public Cliente() {
        super();
    }

    // Constructor básico
    public Cliente(String dni, String nombre, String apellido) {
        super(dni, nombre, apellido);
        this.tipoCliente = "REGULAR";
        this.descuentoEspecial = BigDecimal.ZERO;
    }

    // Constructor con tipo de cliente
    public Cliente(String dni, String nombre, String apellido, String tipoCliente) {
        this(dni, nombre, apellido);
        this.tipoCliente = tipoCliente;
    }

    // Constructor completo
    public Cliente(String idCliente, String codigoCliente, String dni, String nombre,
                   String apellido, LocalDate fechaNacimiento, Genero genero, Contacto contacto,
                   String tipoCliente, BigDecimal descuentoEspecial) {
        super(null, dni, nombre, apellido, fechaNacimiento, genero, contacto);
        this.idCliente = idCliente;
        this.codigoCliente = codigoCliente;
        this.tipoCliente = tipoCliente;
        this.descuentoEspecial = descuentoEspecial != null ? descuentoEspecial : BigDecimal.ZERO;
    }

    // Constructor desde Persona
    public Cliente(Persona persona) {
        super(persona);
        this.tipoCliente = "REGULAR";
        this.descuentoEspecial = BigDecimal.ZERO;
    }

    // Constructor con descuento especial
    public Cliente(String dni, String nombre, String apellido, String tipoCliente,
                   BigDecimal descuentoEspecial) {
        this(dni, nombre, apellido, tipoCliente);
        this.descuentoEspecial = descuentoEspecial;
    }

    // Constructor para clientes regulares
    public static Cliente crearRegular(String dni, String nombre, String apellido, Contacto contacto) {
        Cliente cliente = new Cliente(dni, nombre, apellido, "REGULAR");
        cliente.setContacto(contacto);
        return cliente;
    }

    // Constructor para clientes mayoristas
    public static Cliente crearMayorista(String dni, String nombre, String apellido,
                                         BigDecimal descuentoEspecial) {
        return new Cliente(dni, nombre, apellido, "MAYORISTA", descuentoEspecial);
    }

    // Métodos heredados/abstractos
    @Override
    public String getTipoPersona() {
        return "CLIENTE";
    }

    @Override
    public String getIdentificacionCompleta() {
        return (idCliente != null ? idCliente + " - " : "") + getNombreCompleto();
    }

    @Override
    public String getNombre() {
        return getNombreCompleto();
    }

    @Override
    public boolean isActivo() {
        return true; // Los clientes siempre se consideran activos por defecto
    }

    // Métodos de negocio
    public boolean esMayorista() {
        return "MAYORISTA".equals(tipoCliente);
    }

    public boolean tieneDescuentoEspecial() {
        return descuentoEspecial != null && descuentoEspecial.compareTo(BigDecimal.ZERO) > 0;
    }

    public BigDecimal calcularDescuento(BigDecimal precio) {
        if (!tieneDescuentoEspecial()) return BigDecimal.ZERO;
        return precio.multiply(descuentoEspecial).divide(BigDecimal.valueOf(100));
    }

    public BigDecimal aplicarDescuento(BigDecimal precio) {
        BigDecimal descuento = calcularDescuento(precio);
        return precio.subtract(descuento);
    }

    public void aplicarDescuentoEspecial(BigDecimal descuento) {
        this.descuentoEspecial = descuento;
        actualizarFechaModificacion();
    }

    // Generadores de ID
    public void generarIdCliente() {
        // Formato: C + DNI + año/mes actual
        String mes = String.format("%02d", LocalDate.now().getMonthValue());
        String año = String.valueOf(LocalDate.now().getYear());
        this.idCliente = "C" + dni.replaceAll("\\D", "") + año + mes;
    }

    public void generarCodigoCliente() {
        // Formato: CLI + número secuencial
        int numero = (int)(Math.random() * 9999) + 1;
        this.codigoCliente = "CLI" + String.format("%04d", numero);
    }

    // Getters y Setters
    public String getIdCliente() { return idCliente; }
    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
        actualizarFechaModificacion();
    }

    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
        actualizarFechaModificacion();
    }

    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
        actualizarFechaModificacion();
    }

    public BigDecimal getDescuentoEspecial() { return descuentoEspecial; }
    public void setDescuentoEspecial(BigDecimal descuentoEspecial) {
        this.descuentoEspecial = descuentoEspecial;
        actualizarFechaModificacion();
    }
}
