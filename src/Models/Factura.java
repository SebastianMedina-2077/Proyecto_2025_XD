package Models;

public class Factura extends Comprobante {
    private static int consecutivo = 1;
    private String serie = "F001";
    private String rucCliente;
    private String direccionCliente;

    public String getTipoComprobante() {
        return "FACTURA";
    }

    public boolean requiereRUC() {
        return true;
    }

    public void generarNumero() {
        this.numeroComprobante = String.format("%s-%08d", serie, consecutivo++);
    }

    public String obtenerFormatoImpresion() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("         FACTURA ELECTRONICA           \n");
        sb.append("========================================\n");
        sb.append("Serie: ").append(serie).append("\n");
        sb.append("Numero: ").append(numeroComprobante).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Cliente: ").append(cliente.getRazonSocial()).append("\n");
        sb.append("RUC: ").append(cliente.getDocumento()).append("\n");
        sb.append("Direccion: ").append(cliente.getDireccion()).append("\n");
        sb.append("========================================\n");
        sb.append(String.format("Subtotal: S/ %.2f\n", subtotal));
        sb.append(String.format("IGV (18%%): S/ %.2f\n", igv));
        sb.append(String.format("TOTAL: S/ %.2f\n", total));
        sb.append("========================================\n");
        return sb.toString();
    }

    public String getRucCliente() { return rucCliente; }
    public void setRucCliente(String ruc) { this.rucCliente = ruc; }
    public String getDireccionCliente() { return direccionCliente; }
    public void setDireccionCliente(String dir) { this.direccionCliente = dir; }
}
