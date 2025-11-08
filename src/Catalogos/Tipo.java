package Catalogos;

public class Tipo {
    private Integer id;
    private String nombre;
    private String categoria; // USUARIO, CLIENTE, PROVEEDOR
    private Boolean activo;

    // Constructor por defecto
    public Tipo() {}

    // Constructor básico
    public Tipo(String nombre, String categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.activo = true;
    }

    // Constructor completo
    public Tipo(Integer id, String nombre, String categoria, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.activo = activo;
    }

    // Constructores predefinidos para tipos comunes
    public static Tipo ADMINISTRADOR() {
        return new Tipo(1, "ADMINISTRADOR", "USUARIO", true);
    }

    public static Tipo VENDEDOR() {
        return new Tipo(2, "VENDEDOR", "USUARIO", true);
    }

    public static Tipo REGULAR() {
        return new Tipo(3, "REGULAR", "CLIENTE", true);
    }

    public static Tipo MAYORISTA() {
        return new Tipo(4, "MAYORISTA", "CLIENTE", true);
    }

    public static Tipo PROVEEDOR_GENERAL() {
        return new Tipo(5, "PROVEEDOR_GENERAL", "PROVEEDOR", true);
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
