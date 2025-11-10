package Models;

public class ResultadoLogin {
    private boolean loginExitoso;
    private boolean usuarioBloqueado;
    private boolean usuarioInactivo;
    private boolean credencialesInvalidas;
    private int idUsuario;
    private String nombreCompleto;
    private String rol;
    private int minutosRestantesBloq;
    private String mensaje;

    public ResultadoLogin() {}

    // Getters y Setters
    public boolean isLoginExitoso() { return loginExitoso; }
    public void setLoginExitoso(boolean loginExitoso) { this.loginExitoso = loginExitoso; }

    public boolean isUsuarioBloqueado() { return usuarioBloqueado; }
    public void setUsuarioBloqueado(boolean usuarioBloqueado) { this.usuarioBloqueado = usuarioBloqueado; }

    public boolean isUsuarioInactivo() { return usuarioInactivo; }
    public void setUsuarioInactivo(boolean usuarioInactivo) { this.usuarioInactivo = usuarioInactivo; }

    public boolean isCredencialesInvalidas() { return credencialesInvalidas; }
    public void setCredencialesInvalidas(boolean credencialesInvalidas) {
        this.credencialesInvalidas = credencialesInvalidas;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getMinutosRestantesBloq() { return minutosRestantesBloq; }
    public void setMinutosRestantesBloq(int minutosRestantesBloq) {
        this.minutosRestantesBloq = minutosRestantesBloq;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getMensajeError() {
        if (loginExitoso) return "Login exitoso";
        if (usuarioBloqueado) return "Usuario bloqueado por " + minutosRestantesBloq + " minutos";
        if (usuarioInactivo) return "Usuario inactivo. Contacte al administrador";
        if (credencialesInvalidas) return "Credenciales inválidas";
        return "Error desconocido";
    }
}
