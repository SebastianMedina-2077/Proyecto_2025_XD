package Catalogos;

public class Estado {
    private Integer id;
    private String descripcion;
    private Boolean activo;

    // Constructor por defecto
    public Estado() {}

    // Constructor básico
    public Estado(String descripcion) {
        this.descripcion = descripcion;
        this.activo = true;
    }

    // Constructor completo
    public Estado(Integer id, String descripcion, Boolean activo) {
        this.id = id;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    // Constructores predefinidos
    public static Estado ACTIVO() {
        return new Estado(1, "Activo", true);
    }

    public static Estado INACTIVO() {
        return new Estado(2, "Inactivo", false);
    }

    public static Estado BLOQUEADO() {
        return new Estado(3, "Bloqueado", false);
    }

    // Métodos de negocio
    public boolean estaActivo() {
        return activo != null && activo;
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
