package Strategy;

public class IGVExonerado implements EstrategiaIGV{
    @Override
    public double calcularIGV(double base) {
        return 0.0;
    }

    @Override
    public String getDescripcion() {
        return "Exonerado de IGV";
    }
}
