package Proxy;

import Models.Usuario;
import Models.ResultadoLogin;
import Facade.AuthenticationFacade;
import Repository.UsuarioDAO;

public class AuthenticationProxy {
    private AuthenticationFacade facade;
    private UsuarioDAO usuarioDAO;
    private Usuario usuarioActual;

    public AuthenticationProxy() {
        this.facade = new AuthenticationFacade();
        this.usuarioDAO = new UsuarioDAO();
    }

    // Login con control de acceso
    public ResultadoLogin login(String nombreUsuario, String contrasena, String ipAcceso) {
        // Validaciones previas
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            ResultadoLogin error = new ResultadoLogin();
            error.setLoginExitoso(false);
            error.setMensaje("Debe ingresar un nombre de usuario");
            return error;
        }

        if (contrasena == null || contrasena.trim().isEmpty()) {
            ResultadoLogin error = new ResultadoLogin();
            error.setLoginExitoso(false);
            error.setMensaje("Debe ingresar una contraseña");
            return error;
        }

        // Delegar al facade
        ResultadoLogin resultado = facade.login(nombreUsuario, contrasena, ipAcceso);

        // Si el login es exitoso, cargar usuario actual
        if (resultado.isLoginExitoso()) {
            usuarioActual = usuarioDAO.obtenerPorId(resultado.getIdUsuario());
        }

        return resultado;
    }

    // Verificar si hay sesión activa
    public boolean hayUsuarioActivo() {
        return usuarioActual != null && "Activo".equals(usuarioActual.getEstado());
    }

    // Obtener usuario actual
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Cerrar sesión
    public void cerrarSesion() {
        usuarioActual = null;
    }

    // Verificar permiso antes de ejecutar acción
    public boolean verificarPermiso(String permiso) {
        if (!hayUsuarioActivo()) {
            usuarioDAO.mostrarMensajeError("No hay sesión activa");
            return false;
        }

        boolean tienePermiso = facade.tienePermiso(usuarioActual.getIdUsuario(), permiso);

        if (!tienePermiso) {
            usuarioDAO.mostrarMensajeError("No tiene permisos para realizar esta acción");
        }

        return tienePermiso;
    }

    // Ejecutar acción con verificación de permisos
    public boolean ejecutarConPermiso(String permiso, Runnable accion) {
        if (verificarPermiso(permiso)) {
            accion.run();
            return true;
        }
        return false;
    }
}
