package Clases;

import Catalogos.Estado;
import Catalogos.Tipo;

import java.sql.Date;

public abstract class Persona {
    private String dni;
    private String nombre;
    private String apellido;
    private Date fechanacimiento;
    private String genero;
    private Contacto contacto;
    private Estado estado;
    private Tipo tipo;


    public Persona(String dni, String nombre, String apellido, Date fechanacimiento, String genero, Contacto contacto, Estado estado, Tipo tipo) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechanacimiento = fechanacimiento;
        this.genero = genero;
        this.contacto = contacto;
        this.estado = estado;
        this.tipo = tipo;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public String getGenero() {
        return genero;
    }

    public Contacto getContacto() {
        return contacto;
    }
}
