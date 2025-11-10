package Facade;

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

    // Login simplificado
    public ResultadoLogin login(String nombreUsuario, String contrasena, String ipAcceso) {
        try {
            // Encriptar contraseña para comparar
            String contrasenaEncriptada = encryption.encriptar(contrasena);

            // Validar login usando el DAO
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

    // Registro simplificado
    public boolean registrarUsuario(String nombreUsuario, String contrasena,
                                    String nombreCompleto, String rol,
                                    String telefono, String email) {
        try {
            // Validar que no exista el usuario
            if (usuarioDAO.existeUsuario(nombreUsuario)) {
                usuarioDAO.mostrarMensajeError("El nombre de usuario ya existe");
                return false;
            }

            if (usuarioDAO.existeEmail(email)) {
                usuarioDAO.mostrarMensajeError("El email ya está registrado");
                return false;
            }

            // Encriptar contraseña
            String contrasenaEncriptada = encryption.encriptar(contrasena);

            // Crear usuario
            Usuario usuario = new Usuario(nombreUsuario, contrasenaEncriptada,
                    nombreCompleto, rol, telefono, email);

            // Insertar en base de datos
            return usuarioDAO.insertar(usuario);

        } catch (NoSuchAlgorithmException e) {
            usuarioDAO.mostrarMensajeError("Error al procesar contraseña");
            return false;
        }
    }

    // Recuperar contraseña
    public boolean iniciarRecuperacion(String email) {
        Usuario usuario = usuarioDAO.obtenerPorEmail(email);

        if (usuario == null) {
            usuarioDAO.mostrarMensajeError("No existe ningún usuario con ese email");
            return false;
        }

        return emailService.enviarEmailRecuperacion(email, usuario.getNombreUsuario());
    }

    // Validar token de recuperación
    public boolean validarTokenRecuperacion(String token, String nombreUsuario) {
        return emailService.validarToken(token, nombreUsuario);
    }

    // Restablecer contraseña con token
    public boolean restablecerContrasena(String token, String nombreUsuario, String nuevaContrasena) {
        try {
            // Validar y consumir token
            if (!emailService.consumirToken(token)) {
                usuarioDAO.mostrarMensajeError("Token inválido o expirado");
                return false;
            }

            // Obtener usuario
            Usuario usuario = usuarioDAO.obtenerPorNombreUsuario(nombreUsuario);
            if (usuario == null) {
                usuarioDAO.mostrarMensajeError("Usuario no encontrado");
                return false;
            }

            // Encriptar nueva contraseña
            String contrasenaEncriptada = encryption.encriptar(nuevaContrasena);

            // Actualizar contraseña directamente en BD
            return usuarioDAO.cambiarContrasena(usuario.getIdUsuario(),
                    usuario.getContrasena(),
                    contrasenaEncriptada);

        } catch (NoSuchAlgorithmException e) {
            usuarioDAO.mostrarMensajeError("Error al procesar contraseña");
            return false;
        }
    }

    // Verificar permisos
    public boolean tienePermiso(int idUsuario, String permiso) {
        return usuarioDAO.verificarPermiso(idUsuario, permiso);
    }
}
