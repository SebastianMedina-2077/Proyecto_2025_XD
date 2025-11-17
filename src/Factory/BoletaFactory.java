package Factory;

import Models.Boleta;
import Models.Comprobante;

public class BoletaFactory extends ComprobanteFactory{
    @Override
    public Comprobante crearComprobante() {
        return new Boleta();
    }
}
