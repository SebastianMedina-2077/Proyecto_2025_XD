package Factory;

import Models.Usuario;

public class AlmaceneroFactory extends UsuarioFactory {
    @Override
    public Usuario crearUsuario(String nombreUsuario, String contrasena,
                                String nombreCompleto, String telefono, String email) {
        Usuario usuario = new Usuario(nombreUsuario, contrasena, nombreCompleto,
                "Almacenero", telefono, email);
        return usuario;
    }
}
