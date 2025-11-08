package Clases;

public class ResultadoLogin {
    private boolean exitoso;
    private String mensaje;
    private Usuario usuario;
    private String tipoUsuario;

    public ResultadoLogin(boolean exitoso, String mensaje, Usuario usuario, String tipoUsuario) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.usuario = usuario;
        this.tipoUsuario = tipoUsuario;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean esAdministrador() {
        return "ADMINISTRADOR".equals(tipoUsuario);
    }

    public boolean esVendedor() {
        return "VENDEDOR".equals(tipoUsuario);
    }
}
