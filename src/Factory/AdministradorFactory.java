package Factory;

import Models.Usuario;

public class AdministradorFactory extends UsuarioFactory {
    @Override
    public Usuario crearUsuario(String nombreUsuario, String contrasena,
                                String nombreCompleto, String telefono, String email) {
        Usuario usuario = new Usuario(nombreUsuario, contrasena, nombreCompleto,
                "Administrador", telefono, email);
        return usuario;
    }
}
