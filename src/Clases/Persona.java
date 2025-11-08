package Clases;

import Catalogos.Estado;
import Catalogos.Tipo;
import DB_Conection.EntidadBase;
import Enum.Genero;
import java.sql.Date;
import java.time.LocalDate;

public abstract class Persona extends EntidadBase {
    protected String dni;
    protected String nombre;
    protected String apellido;
    protected LocalDate fechaNacimiento;
    protected Genero genero;
    protected Contacto contacto;

    // Constructor por defecto
    public Persona() {
        super();
    }

    // Constructor básico
    public Persona(String dni, String nombre, String apellido) {
        super();
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // Constructor con fecha de nacimiento
    public Persona(String dni, String nombre, String apellido, LocalDate fechaNacimiento) {
        this(dni, nombre, apellido);
        this.fechaNacimiento = fechaNacimiento;
    }

    // Constructor completo
    public Persona(Integer id, String dni, String nombre, String apellido,
                   LocalDate fechaNacimiento, Genero genero, Contacto contacto) {
        super(id);
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.genero = genero;
        this.contacto = contacto;
    }

    // Constructor desde otra persona
    public Persona(Persona otraPersona) {
        this.dni = otraPersona.dni;
        this.nombre = otraPersona.nombre;
        this.apellido = otraPersona.apellido;
        this.fechaNacimiento = otraPersona.fechaNacimiento;
        this.genero = otraPersona.genero;
        this.contacto = otraPersona.contacto;
        this.fechaRegistro = otraPersona.fechaRegistro;
        this.fechaModificacion = otraPersona.fechaModificacion;
    }

    // Métodos abstractos específicos
    public abstract String getTipoPersona();
    public abstract String getIdentificacionCompleta();

    // Métodos de negocio
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean tieneContacto() {
        return contacto != null;
    }

    public boolean esMayorDeEdad() {
        if (fechaNacimiento == null) return false;
        return LocalDate.now().getYear() - fechaNacimiento.getYear() >= 18;
    }

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return LocalDate.now().getYear() - fechaNacimiento.getYear();
    }

    // Getters y Setters
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre;
        actualizarFechaModificacion();
    }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) {
        this.apellido = apellido;
        actualizarFechaModificacion();
    }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }

    public Contacto getContacto() { return contacto; }
    public void setContacto(Contacto contacto) { this.contacto = contacto; }

}
