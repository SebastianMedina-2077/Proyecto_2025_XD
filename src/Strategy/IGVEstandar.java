package Strategy;

public class IGVEstandar implements EstrategiaIGV{
    private static final double TASA_IGV = 0.18;

    @Override
    public double calcularIGV(double base) {
        return base * TASA_IGV;
    }

    @Override
    public String getDescripcion() {
        return "IGV 18%";
    }
}
