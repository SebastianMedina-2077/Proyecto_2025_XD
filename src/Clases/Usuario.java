package Clases;

import Catalogos.Estado;
import Catalogos.Tipo;
import Interface.Auditable;
import Interface.EntidadConEstado;
import Interface.EntidadConTipo;
import Enum.Genero;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario extends Persona implements EntidadConEstado, EntidadConTipo, Auditable {
    private Integer idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private Estado estado;
    private Tipo tipo;
    private LocalDateTime ultimoIngreso;
    private Integer intentosFallidos;
    private Usuario usuarioCreacion;
    private Usuario usuarioModificacion;

    // Constructor por defecto
    public Usuario() {
        super();
        this.intentosFallidos = 0;
    }

    // Constructor básico para registro
    public Usuario(String nombreUsuario, String contrasena, String dni, String nombre, String apellido) {
        super(dni, nombre, apellido);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.estado = Estado.ACTIVO();
        this.tipo = Tipo.VENDEDOR();
        this.intentosFallidos = 0;
    }

    // Constructor con tipo específico
    public Usuario(String nombreUsuario, String contrasena, String dni, String nombre,
                   String apellido, Tipo tipo) {
        this(nombreUsuario, contrasena, dni, nombre, apellido);
        this.tipo = tipo;
    }

    // Constructor completo
    public Usuario(Integer idUsuario, String nombreUsuario, String contrasena, String dni,
                   String nombre, String apellido, LocalDate fechaNacimiento, Genero genero,
                   Contacto contacto, Estado estado, Tipo tipo, LocalDateTime ultimoIngreso,
                   Integer intentosFallidos) {
        super(idUsuario != null ? idUsuario : null, dni, nombre, apellido, fechaNacimiento, genero, contacto);
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.estado = estado != null ? estado : Estado.ACTIVO();
        this.tipo = tipo != null ? tipo : Tipo.VENDEDOR();
        this.ultimoIngreso = ultimoIngreso;
        this.intentosFallidos = intentosFallidos != null ? intentosFallidos : 0;
    }

    // Constructor desde Persona
    public Usuario(Persona persona, String nombreUsuario, String contrasena) {
        super(persona);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.estado = Estado.ACTIVO();
        this.tipo = Tipo.VENDEDOR();
        this.intentosFallidos = 0;
    }

    // Constructor para administradores
    public static Usuario crearAdministrador(String nombreUsuario, String contrasena, String dni,
                                             String nombre, String apellido, Contacto contacto) {
        Usuario admin = new Usuario(nombreUsuario, contrasena, dni, nombre, apellido, Tipo.ADMINISTRADOR());
        admin.setContacto(contacto);
        return admin;
    }

    // Métodos heredados/abstractos
    @Override
    public String getTipoPersona() {
        return "USUARIO";
    }

    @Override
    public String getIdentificacionCompleta() {
        return dni + " - " + nombreUsuario;
    }

    @Override
    public String getNombre() {
        return getNombreCompleto();
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

    @Override
    public Tipo getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
        actualizarFechaModificacion();
    }

    // Métodos de auditoría
    @Override
    public Usuario getUsuarioCreacion() {
        return usuarioCreacion;
    }


    public void setUsuarioCreacion(Usuario usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    @Override
    public Usuario getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuario usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
        actualizarFechaModificacion();
    }

    @Override
    public LocalDateTime getFechaCreacion() {
        return getFechaRegistro();
    }

    // Métodos de negocio específicos
    public boolean validarContrasena(String contrasenaIngresada) {
        return this.contrasena.equals(contrasenaIngresada);
    }

    public boolean esAdministrador() {
        return tipo != null && "ADMINISTRADOR".equals(tipo.getNombre());
    }

    public boolean esVendedor() {
        return tipo != null && "VENDEDOR".equals(tipo.getNombre());
    }

    public void registrarIntentoFallido() {
        this.intentosFallidos++;
        actualizarFechaModificacion();
    }

    public void resetearIntentos() {
        this.intentosFallidos = 0;
        actualizarFechaModificacion();
    }

    public void registrarIngreso() {
        this.ultimoIngreso = LocalDateTime.now();
        resetearIntentos();
    }

    public boolean estaBloqueado() {
        return intentosFallidos >= 3;
    }

    public void cambiarContrasena(String nuevaContrasena, Usuario usuarioModificacion) {
        this.contrasena = nuevaContrasena;
        setUsuarioModificacion(usuarioModificacion);
    }

    // Getters y Setters específicos
    public Integer getIdUsuario() { return idUsuario != null ? idUsuario : getId(); }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
        setId(idUsuario);
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
        actualizarFechaModificacion();
    }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
        actualizarFechaModificacion();
    }

    public LocalDateTime getUltimoIngreso() { return ultimoIngreso; }
    public void setUltimoIngreso(LocalDateTime ultimoIngreso) { this.ultimoIngreso = ultimoIngreso; }

    public Integer getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(Integer intentosFallidos) { this.intentosFallidos = intentosFallidos; }

}
