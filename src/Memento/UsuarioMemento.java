package Memento;

import Models.Usuario;
import java.time.LocalDateTime;

public class UsuarioMemento {
    private final int idUsuario;
    private final String nombreUsuario;
    private final String nombreCompleto;
    private final String rol;
    private final String telefono;
    private final String email;
    private final String estado;
    private final LocalDateTime fechaCreacion;

    public UsuarioMemento(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.rol = usuario.getRol();
        this.telefono = usuario.getTelefono();
        this.email = usuario.getEmail();
        this.estado = usuario.getEstado();
        this.fechaCreacion = LocalDateTime.now();
    }

    public Usuario restaurar() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(this.idUsuario);
        usuario.setNombreUsuario(this.nombreUsuario);
        usuario.setNombreCompleto(this.nombreCompleto);
        usuario.setRol(this.rol);
        usuario.setTelefono(this.telefono);
        usuario.setEmail(this.email);
        usuario.setEstado(this.estado);
        return usuario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
