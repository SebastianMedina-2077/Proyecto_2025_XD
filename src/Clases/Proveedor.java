package Clases;

import Catalogos.*;

import DB_Conection.EntidadBase;
import Interface.EntidadConEstado;

public class Proveedor extends EntidadBase implements EntidadConEstado {
    private Integer codigoProveedor;
    private String razonSocial;
    private String ruc;
    private Persona contacto; // Relación con Persona
    private String banco;
    private String numeroCuenta;
    private Estado estado;
    private Tipo tipo;

    // Constructor por defecto
    public Proveedor() {
        super();
        this.estado = Estado.ACTIVO();
        this.tipo = Tipo.PROVEEDOR_GENERAL();
    }

    // Constructor básico
    public Proveedor(String razonSocial, String ruc) {
        super();
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.estado = Estado.ACTIVO();
        this.tipo = Tipo.PROVEEDOR_GENERAL();
    }

    // Constructor completo
    public Proveedor(Integer codigoProveedor, String razonSocial, String ruc, Persona contacto,
                     String banco, String numeroCuenta, Estado estado, Tipo tipo) {
        super(codigoProveedor);
        this.codigoProveedor = codigoProveedor;
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.contacto = contacto;
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        this.estado = estado != null ? estado : Estado.ACTIVO();
        this.tipo = tipo != null ? tipo : Tipo.PROVEEDOR_GENERAL();
    }

    // Constructor desde Persona
    public Proveedor(Persona persona, String razonSocial, String ruc) {
        super();
        this.razonSocial = razonSocial;
        this.ruc = ruc;
        this.contacto = persona;
        this.estado = Estado.ACTIVO();
        this.tipo = Tipo.PROVEEDOR_GENERAL();
    }

    // Métodos abstractos/interface
    @Override
    public String getNombre() {
        return razonSocial;
    }

    @Override
    public boolean isActivo() {
        return estado != null && estado.estaActivo();
    }

    @Override
    public Estado getEstado() {
        return estado;
    }

    @Override
    public void setEstado(Estado estado) {
        this.estado = estado;
        actualizarFechaModificacion();
    }

    @Override
    public boolean estaActivo() {
        return isActivo();
    }

    // Métodos de negocio
    public boolean tieneContactoBancario() {
        return banco != null && !banco.trim().isEmpty() &&
                numeroCuenta != null && !numeroCuenta.trim().isEmpty();
    }

    public boolean esRucValido() {
        return ruc != null && ruc.matches("\\d{11}");
    }

    public void actualizarDatosBancarios(String banco, String numeroCuenta) {
        this.banco = banco;
        this.numeroCuenta = numeroCuenta;
        actualizarFechaModificacion();
    }

    // Getters y Setters
    public Integer getCodigoProveedor() { return codigoProveedor != null ? codigoProveedor : getId(); }
    public void setCodigoProveedor(Integer codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
        setId(codigoProveedor);
    }

    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
        actualizarFechaModificacion();
    }

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) {
        this.ruc = ruc;
        actualizarFechaModificacion();
    }

    public Persona getContacto() { return contacto; }
    public void setContacto(Persona contacto) { this.contacto = contacto; }

    public String getBanco() { return banco; }
    public void setBanco(String banco) {
        this.banco = banco;
        actualizarFechaModificacion();
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
        actualizarFechaModificacion();
    }

    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
        actualizarFechaModificacion();
    }
}
