package Clases;

public class Cliente extends Persona {
    String idcliente;
    String codigo;
    String tipocliente;

    public Cliente(String idcliente, String codigo, String tipocliente, String dni, String nombre, String apellido, java.sql.Date fechanacimiento, String genero, Contacto contacto, Catalogos.Estado estado, Catalogos.Tipo tipo) {
        super(dni, nombre, apellido, fechanacimiento, genero, contacto, estado, tipo);
        this.idcliente = idcliente;
        this.codigo = codigo;
        this.tipocliente = tipocliente;
    }
}
