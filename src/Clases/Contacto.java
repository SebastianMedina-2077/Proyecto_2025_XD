package Clases;

public class Contacto extends Direccion {
    private int idContacto;
    private String numeroTelefonico;
    private String operador;
    private String correoElectronico;
    private String whatsapp;

    public Contacto(String numeroTelefonico, String operador, String correoElectronico, String whatsapp, String direccion, String departamento, String provincia, String distrito, String referencia) {
        super(direccion, departamento, provincia, distrito, referencia);
        this.numeroTelefonico = numeroTelefonico;
        this.operador = operador;
        this.correoElectronico = correoElectronico;
        this.whatsapp = whatsapp;
    }


    public String getNumeroTelefonico() {
        return numeroTelefonico;
    }

    public String getOperador() {
        return operador;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getWhatsapp() {
        return whatsapp;
    }
}
