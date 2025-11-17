package Facade;

import DB_Conection.DatabaseConnection;
import Models.Usuario;
import Models.ResultadoLogin;
import Repository.UsuarioDAO;
import Services.EmailService;
import Security.PasswordEncryption;
import java.security.NoSuchAlgorithmException;

public class AuthenticationFacade {
    private UsuarioDAO usuarioDAO;
    private EmailService emailService;
    private PasswordEncryption encryption;

    public AuthenticationFacade() {
        this.usuarioDAO = new UsuarioDAO();
        this.emailService = EmailService.getInstance();
        this.encryption = new PasswordEncryption();
    }

    public ResultadoLogin login(String nombreUsuario, String contrasena, String ipAcceso) {
        try {
            String contrasenaEncriptada = encryption.encriptar(contrasena);

            ResultadoLogin resultado = usuarioDAO.validarLogin(nombreUsuario,
                    contrasenaEncriptada, ipAcceso);

            return resultado;
        } catch (NoSuchAlgorithmException e) {
            ResultadoLogin error = new ResultadoLogin();
            error.setLoginExitoso(false);
            error.setMensaje("Error al procesar credenciales");
            return error;
        }
    }

    public boolean registrarUsuario(String nombreUsuario, String contrasena,
                                    String nombreCompleto, String rol,
                                    String telefono, String email) {
        try {
            if (usuarioDAO.existeUsuario(nombreUsuario)) {
                usuarioDAO.mostrarMensajeError("El nombre de usuario ya existe");
                return false;
            }

            if (usuarioDAO.existeEmail(email)) {
                usuarioDAO.mostrarMensajeError("El email ya esta registrado");
                return false;
            }

            String contrasenaEncriptada = encryption.encriptar(contrasena);

            Usuario usuario = new Usuario(nombreUsuario, contrasenaEncriptada,
                    nombreCompleto, rol, telefono, email);

            boolean insertado = usuarioDAO.insertar(usuario);

            if (insertado) {
                refrescarConexion();
            }

            return insertado;

        } catch (NoSuchAlgorithmException e) {
            usuarioDAO.mostrarMensajeError("Error al procesar contrasena");
            return false;
        }
    }

    public boolean iniciarRecuperacion(String email) {
        Usuario usuario = usuarioDAO.obtenerPorEmail(email);

        if (usuario == null) {
            usuarioDAO.mostrarMensajeError("No existe ningun usuario con ese email");
            return false;
        }

        return emailService.enviarEmailRecuperacion(email, usuario.getNombreUsuario());
    }

    public boolean validarTokenRecuperacion(String token, String nombreUsuario) {
        return emailService.validarToken(token, nombreUsuario);
    }

    public boolean restablecerContrasena(String token, String nombreUsuario, String nuevaContrasena) {
        try {
            if (!emailService.consumirToken(token)) {
                usuarioDAO.mostrarMensajeError("Token invalido o expirado");
                return false;
            }

            Usuario usuario = usuarioDAO.obtenerPorNombreUsuario(nombreUsuario);
            if (usuario == null) {
                usuarioDAO.mostrarMensajeError("Usuario no encontrado");
                return false;
            }

            String contrasenaEncriptada = encryption.encriptar(nuevaContrasena);

            boolean actualizado = usuarioDAO.cambiarContrasena(usuario.getIdUsuario(),
                    usuario.getContrasena(),
                    contrasenaEncriptada);

            if (actualizado) {
                refrescarConexion();
            }

            return actualizado;

        } catch (NoSuchAlgorithmException e) {
            usuarioDAO.mostrarMensajeError("Error al procesar contrasena");
            return false;
        }
    }

    public boolean tienePermiso(int idUsuario, String permiso) {
        return usuarioDAO.verificarPermiso(idUsuario, permiso);
    }

    private void refrescarConexion() {
        try {
            DatabaseConnection.getInstance().getConnection();
        } catch (Exception e) {
            System.err.println("Advertencia al refrescar conexion: " + e.getMessage());
        }
    }
}
