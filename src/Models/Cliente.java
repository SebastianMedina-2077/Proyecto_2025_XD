package Models;

import java.time.LocalDate;

public class Cliente implements Cloneable {
    private int idCliente;
    private String tipoCliente;
    private String razonSocial;
    private String documento;
    private String tipoDocumento;
    private String telefono;
    private String email;
    private String direccion;
    private double descuentoPersonalizado;
    private LocalDate fechaRegistro;
    private String estado;

    public Cliente() {
    }

    public Cliente(String tipoCliente, String razonSocial, String documento,
                   String tipoDocumento, String telefono, String email, String direccion) {
        this.tipoCliente = tipoCliente;
        this.razonSocial = razonSocial;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
        this.descuentoPersonalizado = 0.0;
        this.estado = "Activo";
    }

    @Override
    public Cliente clone() {
        try {
            Cliente clonado = (Cliente) super.clone();
            clonado.idCliente = 0;
            clonado.documento = "";
            clonado.fechaRegistro = null;
            return clonado;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error al clonar cliente", e);
        }
    }

    public Cliente clonarParaTipoCliente(String nuevoTipo) {
        Cliente clonado = this.clone();
        clonado.setTipoCliente(nuevoTipo);
        return clonado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getDescuentoPersonalizado() {
        return descuentoPersonalizado;
    }

    public void setDescuentoPersonalizado(double descuentoPersonalizado) {
        this.descuentoPersonalizado = descuentoPersonalizado;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return razonSocial + " - " + documento;
    }
}
