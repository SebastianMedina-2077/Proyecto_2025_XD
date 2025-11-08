package Clases;

public class Contacto {
    private Integer id;
    private String numeroTel;
    private String operador;
    private String correo;
    private String whatsapp;
    private Direccion direccion;

    // Constructor por defecto
    public Contacto() {}

    // Constructor básico
    public Contacto(String numeroTel, String correo) {
        this.numeroTel = numeroTel;
        this.correo = correo;
    }

    // Constructor completo
    public Contacto(Integer id, String numeroTel, String operador, String correo,
                    String whatsapp, Direccion direccion) {
        this.id = id;
        this.numeroTel = numeroTel;
        this.operador = operador;
        this.correo = correo;
        this.whatsapp = whatsapp;
        this.direccion = direccion;
    }

    // Constructor para WhatsApp
    public Contacto(String numeroTel, String whatsapp, String correo) {
        this.numeroTel = numeroTel;
        this.whatsapp = whatsapp;
        this.correo = correo;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumeroTel() { return numeroTel; }
    public void setNumeroTel(String numeroTel) { this.numeroTel = numeroTel; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getWhatsapp() { return whatsapp; }
    public void setWhatsapp(String whatsapp) { this.whatsapp = whatsapp; }

    public Direccion getDireccion() { return direccion; }
    public void setDireccion(Direccion direccion) { this.direccion = direccion; }

}
