package Clases;

public class Direccion {
    private Integer id;
    private String direccionCompleta;
    private String departamento;
    private String provincia;
    private String distrito;
    private String referencia;

    // Constructor por defecto
    public Direccion() {}

    // Constructor básico
    public Direccion(String direccionCompleta) {
        this.direccionCompleta = direccionCompleta;
    }

    // Constructor con todos los campos
    public Direccion(Integer id, String direccionCompleta, String departamento,
                     String provincia, String distrito, String referencia) {
        this.id = id;
        this.direccionCompleta = direccionCompleta;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.referencia = referencia;
    }

    // Constructor por ciudades
    public Direccion(String direccionCompleta, String departamento, String provincia, String distrito) {
        this.direccionCompleta = direccionCompleta;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDireccionCompleta() { return direccionCompleta; }
    public void setDireccionCompleta(String direccionCompleta) { this.direccionCompleta = direccionCompleta; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
