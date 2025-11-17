package Flyweight;

public class ProductoFlyweight {
    private String codigoProducto;
    private String nombreProducto;
    private double precioUnidad;
    private CaracteristicasCompartidas caracteristicas;

    public ProductoFlyweight(String codigoProducto, String nombreProducto,
                             double precioUnidad, CaracteristicasCompartidas caracteristicas) {
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.precioUnidad = precioUnidad;
        this.caracteristicas = caracteristicas;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public CaracteristicasCompartidas getCaracteristicas() {
        return caracteristicas;
    }

    public void mostrarInfo() {
        System.out.println("Producto: " + nombreProducto);
        System.out.println("Codigo: " + codigoProducto);
        System.out.println("Precio: S/ " + precioUnidad);
        System.out.println("Categoria: " + caracteristicas.getCategoria());
        System.out.println("Marca: " + caracteristicas.getMarca());
        System.out.println("Edad: " + caracteristicas.getEdadRecomendada());
        System.out.println("Peso: " + caracteristicas.getPesoKg() + " kg");
    }
}
