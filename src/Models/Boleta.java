package Models;

public class Boleta extends Comprobante {
    private static int consecutivo = 1;
    private String serie = "B001";

    @Override
    public String getTipoComprobante() {
        return "BOLETA DE VENTA";
    }

    @Override
    public boolean requiereRUC() {
        return false;
    }

    @Override
    public void generarNumero() {
        this.numeroComprobante = String.format("%s-%08d", serie, consecutivo++);
    }

    @Override
    public String obtenerFormatoImpresion() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         BOLETA DE VENTA ELECTRONICA   \n");
        sb.append("========================================\n");
        sb.append("Serie: ").append(serie).append("\n");
        sb.append("Numero: ").append(numeroComprobante).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Cliente: ").append(cliente.getRazonSocial()).append("\n");
        if (cliente.getDocumento() != null) {
            sb.append("DNI: ").append(cliente.getDocumento()).append("\n");
        }
        sb.append("========================================\n");
        sb.append(String.format("Subtotal: S/ %.2f\n", subtotal));
        sb.append(String.format("IGV (18%%): S/ %.2f\n", igv));
        sb.append(String.format("TOTAL: S/ %.2f\n", total));
        sb.append("========================================\n");
        return sb.toString();
    }
}
