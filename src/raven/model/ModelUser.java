package raven.model;

/**
 *
 * @author Raven
 */
public class ModelUser {

    private int idUsuario;
    private String userName;
    private String nombreCompleto;
    private String rol;
    private boolean admin;
    private String email;
    private String telefono;

    public ModelUser(String userName, boolean admin) {
        this.userName = userName;
        this.admin = admin;
    }

    public ModelUser(int idUsuario, String userName, String nombreCompleto,
                     String rol, String email, String telefono) {
        this.idUsuario = idUsuario;
        this.userName = userName;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
        this.admin = "Administrador".equals(rol);
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) {
        this.rol = rol;
        this.admin = "Administrador".equals(rol);
    }

    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return "ModelUser{" +
                "userName='" + userName + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rol='" + rol + '\'' +
                ", admin=" + admin +
                '}';
    }
}
