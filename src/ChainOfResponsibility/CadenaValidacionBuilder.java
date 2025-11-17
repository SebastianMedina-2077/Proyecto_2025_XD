package ChainOfResponsibility;

public class CadenaValidacionBuilder {
    private ValidadorVenta primero;
    private ValidadorVenta ultimo;

    public CadenaValidacionBuilder agregarValidador(ValidadorVenta validador) {
        if (primero == null) {
            primero = validador;
            ultimo = validador;
        } else {
            ultimo.setSiguiente(validador);
            ultimo = validador;
        }
        return this;
    }

    public ValidadorVenta construir() {
        return primero;
    }

    public static ValidadorVenta cadenaCompleta() {
        return new CadenaValidacionBuilder()
                .agregarValidador(new ValidadorCliente())
                .agregarValidador(new ValidadorStock())
                .agregarValidador(new ValidadorMontoMinimo())
                .agregarValidador(new ValidadorMedioPago())
                .agregarValidador(new ValidadorLimiteCredito())
                .construir();
    }
}
