package Clases;

import Catalogos.Estado;
import Catalogos.Tipo;

import java.sql.Date;
import java.time.LocalDateTime;

public class Usuario extends Persona {
    private String nombreUsuario;
    private String contrasena;
    private LocalDateTime ultimoAcceso;
    private int IntentosFallidos;

    public Usuario(String dni, String nombre, String apellido, Date fechanacimiento, String genero, Contacto contacto, Estado estado, Tipo tipo, String nombreUsuario, String contrasena, LocalDateTime ultimoAcceso, int intentosFallidos) {
        super(dni, nombre, apellido, fechanacimiento, genero, contacto, estado, tipo);
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.ultimoAcceso = ultimoAcceso;
        IntentosFallidos = intentosFallidos;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public LocalDateTime getUltimoAcceso() {
        return ultimoAcceso;
    }

    public int getIntentosFallidos() {
        return IntentosFallidos;
    }
}
