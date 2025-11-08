package Enum;

public enum TipoMovimiento {
    ENTRADA("ENTRADA"),
    SALIDA("SALIDA"),
    AJUSTE("AJUSTE");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
