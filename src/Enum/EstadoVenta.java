package Enum;

public enum EstadoVenta {
    PENDIENTE("PENDIENTE"),
    COMPLETADA("COMPLETADA"),
    CANCELADA("CANCELADA"),
    ANULADA("ANULADA");

    private final String descripcion;

    EstadoVenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
