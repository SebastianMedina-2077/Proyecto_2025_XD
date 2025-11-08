package Singleton;

import Clases.Usuario;

import java.time.LocalDateTime;

public class SesionManager {
    private static SesionManager instancia;
    private Usuario usuarioActual;
    private LocalDateTime horaInicio;
    private LocalDateTime ultimaActividad;

    private SesionManager() {
        // Constructor privado
    }

    public static SesionManager obtenerInstancia() {
        if (instancia == null) {
            synchronized (SesionManager.class) {
                if (instancia == null) {
                    instancia = new SesionManager();
                }
            }
        }
        return instancia;
    }

    /**
     * Inicia sesión para un usuario
     */
    public void iniciarSesion(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        this.usuarioActual = usuario;
        this.horaInicio = LocalDateTime.now();
        this.ultimaActividad = LocalDateTime.now();

        // Registrar en el usuario
        usuario.registrarIngreso();
    }

    /**
     * Cierra la sesión actual
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
        this.horaInicio = null;
        this.ultimaActividad = null;
    }

    /**
     * Actualiza la última actividad
     */
    public void actualizarActividad() {
        if (usuarioActual != null) {
            this.ultimaActividad = LocalDateTime.now();
        }
    }

    /**
     * Verifica si hay una sesión activa
     */
    public boolean haySesionActiva() {
        return usuarioActual != null;
    }

    /**
     * Obtiene el usuario actualmente autenticado
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Verifica si el usuario actual es administrador
     */
    public boolean esAdministrador() {
        return usuarioActual != null && usuarioActual.esAdministrador();
    }

    /**
     * Verifica si el usuario actual es vendedor
     */
    public boolean esVendedor() {
        return usuarioActual != null && usuarioActual.esVendedor();
    }

    /**
     * Obtiene el ID del usuario actual
     */
    public Integer getIdUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getIdUsuario() : null;
    }

    /**
     * Obtiene el nombre completo del usuario actual
     */
    public String getNombreUsuarioActual() {
        return usuarioActual != null ? usuarioActual.getNombreCompleto() : "Sin usuario";
    }

    /**
     * Verifica el tiempo de inactividad en minutos
     */
    public long getMinutosInactividad() {
        if (ultimaActividad == null) return 0;
        return java.time.Duration.between(ultimaActividad, LocalDateTime.now()).toMinutes();
    }

    /**
     * Verifica si la sesión ha expirado
     */
    public boolean sesionExpirada(int minutosExpiracion) {
        return getMinutosInactividad() >= minutosExpiracion;
    }

    /**
     * Obtiene la hora de inicio de la sesión
     */
    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    /**
     * Obtiene la última actividad
     */
    public LocalDateTime getUltimaActividad() {
        return ultimaActividad;
    }

    /**
     * Actualiza el usuario actual (por ejemplo, después de modificar sus datos)
     */
    public void actualizarUsuario(Usuario usuarioActualizado) {
        if (this.usuarioActual != null &&
                this.usuarioActual.getIdUsuario().equals(usuarioActualizado.getIdUsuario())) {
            this.usuarioActual = usuarioActualizado;
        }
    }
}
