package Clases;

public class Direccion {
    private String direccion;
    private String departamento;
    private String provincia;
    private String distrito;
    private String referencia;

    public Direccion(String direccion, String departamento, String provincia, String distrito, String referencia) {
        this.direccion = direccion;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.referencia = referencia;
    }


    public String getDireccion() {
        return direccion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public String getReferencia() {
        return referencia;
    }
}
