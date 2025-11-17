package Factory;

import Models.Comprobante;
import Models.Factura;

public class FacturaFactory extends ComprobanteFactory{
    @Override
    public Comprobante crearComprobante() {
        return new Factura();
    }
}
