package Servicios;

import Clases.Persona;
import Clases.ResultadoLogin;
import Clases.Usuario;
import Clases_DAO.PersonaDAO;
import Clases_DAO.UsuarioDAO;
import DB_Conection.ValidadorAdaptado;
import Singleton.SesionManager;

public class UsuarioServicio extends ValidadorAdaptado {
    private final PersonaDAO personaDAO;
    private final UsuarioDAO usuarioDAO;
    private final SesionManager sesionManager;

    public UsuarioServicio() {
        this.personaDAO = new PersonaDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.sesionManager = SesionManager.obtenerInstancia();
    }

    /**
     * Registra un nuevo usuario completo (Persona + Usuario)
     */
    public Usuario registrarUsuarioCompleto(Usuario usuario, Integer idUsuarioAdmin) {
        if (usuario == null) {
            mostrarMensajeError("El usuario no puede ser null");
            return null;
        }

        // PASO 1: Verificar si la persona ya existe
        boolean personaExiste = personaDAO.existePersonaPorDni(usuario.getDni());

        if (!personaExiste) {
            mostrarMensajeInformacion("Registrando datos personales...");

            // Crear Persona desde Usuario
            Persona persona = crearPersonaDesdeUsuario(usuario);
            Persona personaRegistrada = personaDAO.guardar(persona);

            if (personaRegistrada == null) {
                mostrarMensajeError("Error al registrar los datos personales");
                return null;
            }
        }

        // PASO 2: Verificar que no exista ya un usuario con ese DNI
        Usuario usuarioExistente = usuarioDAO.obtenerUsuarioPorDni(usuario.getDni());
        if (usuarioExistente != null) {
            mostrarMensajeError("Ya existe un usuario registrado con el DNI: " + usuario.getDni());
            return null;
        }

        // PASO 3: Registrar el usuario
        Usuario usuarioRegistrado = usuarioDAO.guardar(usuario);

        if (usuarioRegistrado != null) {
            mostrarMensajeExito("Usuario registrado exitosamente");
            return usuarioRegistrado;
        } else {
            mostrarMensajeError("Error al crear el usuario");
            return null;
        }
    }

    /**
     * Valida las credenciales y retorna el resultado del login
     */
    public ResultadoLogin iniciarSesion(String nombreUsuario, String contrasena) {
        if (!ValidarCampoVacio(nombreUsuario, "Nombre de Usuario") ||
                !ValidarCampoVacio(contrasena, "Contraseña")) {
            return new ResultadoLogin(false, "Complete todos los campos", null, null);
        }

        ResultadoLogin resultado = usuarioDAO.validarLogin(nombreUsuario, contrasena);

        if (resultado.isExitoso()) {
            sesionManager.iniciarSesion(resultado.getUsuario());
            mostrarMensajeExito("¡Bienvenido " + resultado.getUsuario().getNombreCompleto() + "!");
        } else {
            mostrarMensajeError(resultado.getMensaje());
        }

        return resultado;
    }

    /**
     * Cierra la sesión del usuario actual
     */
    public void cerrarSesion() {
        if (sesionManager.haySesionActiva()) {
            String nombreUsuario = sesionManager.getNombreUsuarioActual();
            sesionManager.cerrarSesion();
            mostrarMensajeInformacion("Sesión cerrada. Hasta pronto " + nombreUsuario);
        }
    }

    /**
     * Obtiene el usuario actualmente autenticado
     */
    public static Usuario getUsuarioActual() {
        return SesionManager.obtenerInstancia().getUsuarioActual();
    }

    /**
     * Verifica si hay un usuario autenticado
     */
    public boolean hayUsuarioAutenticado() {
        return sesionManager.haySesionActiva();
    }

    /**
     * Verifica si el usuario actual es administrador
     */
    public boolean esAdministrador() {
        return sesionManager.esAdministrador();
    }

    // Crear Persona desde Usuario
    private Persona crearPersonaDesdeUsuario(Usuario usuario) {
        return new Persona(
                usuario.getId(),
                usuario.getDni(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getFechaNacimiento(),
                usuario.getGenero(),
                usuario.getContacto()
        ) {
            @Override
            public String getTipoPersona() {
                return "PERSONA";
            }

            @Override
            public String getIdentificacionCompleta() {
                return getDni() + " - " + getNombreCompleto();
            }

            @Override
            public boolean isActivo() {
                return true;
            }
        };
    }
}
