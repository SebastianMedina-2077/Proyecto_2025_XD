package Interface;

import Clases.Usuario;

import java.util.List;

public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {
    Usuario validarLogin(String nombreUsuario, String contrasena);
    List<Usuario> listarPorTipo(String nombreTipo);
    void incrementarIntentosFallidos(Integer idUsuario);
    void resetearIntentosFallidos(Integer idUsuario);
}
