package Flyweight;

public class CaracteristicasCompartidas {
    private final String categoria;
    private final String marca;
    private final String edadRecomendada;
    private final double pesoKg;

    public CaracteristicasCompartidas(String categoria, String marca,
                                      String edadRecomendada, double pesoKg) {
        this.categoria = categoria;
        this.marca = marca;
        this.edadRecomendada = edadRecomendada;
        this.pesoKg = pesoKg;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getMarca() {
        return marca;
    }

    public String getEdadRecomendada() {
        return edadRecomendada;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CaracteristicasCompartidas that = (CaracteristicasCompartidas) obj;
        return Double.compare(that.pesoKg, pesoKg) == 0 &&
                categoria.equals(that.categoria) &&
                marca.equals(that.marca) &&
                edadRecomendada.equals(that.edadRecomendada);
    }

    @Override
    public int hashCode() {
        int result = categoria.hashCode();
        result = 31 * result + marca.hashCode();
        result = 31 * result + edadRecomendada.hashCode();
        result = 31 * result + Double.hashCode(pesoKg);
        return result;
    }
}
