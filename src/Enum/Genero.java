package Enum;

public enum Genero {
    M('M', "Masculino"),
    F('F', "Femenino");

    private final char codigo;
    private final String descripcion;

    Genero(char codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public char getCodigo() {
        return codigo;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
