package Enum;

public enum MetodoPago {
    EFECTIVO("EFECTIVO"),
    TARJETA("TARJETA"),
    TRANSFERENCIA("TRANSFERENCIA"),
    CREDITO("CRÉDITO");

    private final String descripcion;

    MetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
