package Factory;

import Models.Usuario;

public abstract class UsuarioFactory {
    public abstract Usuario crearUsuario(String nombreUsuario, String contrasena,
                                         String nombreCompleto, String telefono, String email);

    // Método plantilla
    public Usuario registrarUsuario(String nombreUsuario, String contrasena,
                                    String nombreCompleto, String telefono, String email) {
        Usuario usuario = crearUsuario(nombreUsuario, contrasena, nombreCompleto, telefono, email);
        establecerConfiguracionPorDefecto(usuario);
        return usuario;
    }

    protected void establecerConfiguracionPorDefecto(Usuario usuario) {
        usuario.setEstado("Activo");
        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
    }
}
