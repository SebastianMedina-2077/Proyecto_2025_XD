package Factory;

public class ComprobanteFactoryProvider {
    public static ComprobanteFactory getFactory(String tipoDocumento) {
        if (tipoDocumento == null) {
            return new BoletaFactory();
        }

        return switch (tipoDocumento.toUpperCase()) {
            case "RUC" -> new FacturaFactory();
            default -> new BoletaFactory();
        };
    }

    public static ComprobanteFactory getFactoryPorMonto(double total) {
        if (total >= 700.00) {
            return new FacturaFactory();
        }
        return new BoletaFactory();
    }
}
