package Clases;

public class Proveedor extends Persona {
    private String razonsocial;
    private String ruc;
    private String banco_proveedor;
    private String cuenta_bancaria;

    public Proveedor(String razonsocial, String ruc, String banco_proveedor, String cuenta_bancaria, String dni, String nombre, String apellido, java.sql.Date fechanacimiento, String genero, int contacto, Catalogos.Estado estado, Catalogos.Tipo tipo) {
        super(dni, nombre, apellido, fechanacimiento, genero, contacto, estado, tipo);
        this.razonsocial = razonsocial;
        this.ruc = ruc;
        this.banco_proveedor = banco_proveedor;
        this.cuenta_bancaria = cuenta_bancaria;
    }


    public String getRazonsocial() {
        return razonsocial;
    }

    public String getRuc() {
        return ruc;
    }

    public String getBanco_proveedor() {
        return banco_proveedor;
    }

    public String getCuenta_bancaria() {
        return cuenta_bancaria;
    }
}
