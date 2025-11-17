package Template_Method;

import Models.CalculadoraVenta;
import Models.DetalleVenta;
import Models.Venta;
import Repository.VentaRepository;

public abstract class ProcesoVenta {
    protected Venta venta;
    protected VentaRepository ventaRepo;
    protected CalculadoraVenta calculadora;

    public ProcesoVenta() {
        this.venta = new Venta();
        this.ventaRepo = new VentaRepository();
        this.calculadora = new CalculadoraVenta();
    }

    // Template Method - Define el esqueleto del algoritmo
    public final boolean ejecutarVenta() {
        try {
            if (!validarPreventa()) {
                return false;
            }

            if (!seleccionarCliente()) {
                return false;
            }

            if (!seleccionarProductos()) {
                return false;
            }

            if (!validarStock()) {
                return false;
            }

            if (!calcularTotales()) {
                return false;
            }

            if (!seleccionarMedioPago()) {
                return false;
            }

            if (!confirmarVenta()) {
                return false;
            }

            if (!registrarVenta()) {
                return false;
            }

            postProcesamiento();
            return true;

        } catch (Exception e) {
            manejarError(e);
            return false;
        }
    }

    // Métodos abstractos - deben ser implementados por subclases
    protected abstract boolean validarPreventa();
    protected abstract boolean seleccionarCliente();
    protected abstract boolean seleccionarProductos();

    // Métodos con implementación por defecto - pueden ser sobrescritos
    protected boolean validarStock() {
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getCantidad() > detalle.getProducto().getStockTotal()) {
                System.out.println("Stock insuficiente para: " +
                        detalle.getProducto().getNombreProducto());
                return false;
            }
        }
        return true;
    }

    protected boolean calcularTotales() {
        double subtotal = venta.getDetalles().stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();

        CalculadoraVenta.ResultadoCalculo resultado =
                calculadora.calcular(subtotal, venta.getCliente(), venta.getDetalles());

        venta.setSubtotal(resultado.getSubtotal());
        venta.setDescuentoTotal(resultado.getDescuento());
        venta.setIgv(resultado.getIgv());
        venta.setTotal(resultado.getTotal());

        return true;
    }

    protected abstract boolean seleccionarMedioPago();
    protected abstract boolean confirmarVenta();

    protected boolean registrarVenta() {
        return ventaRepo.registrarVenta(venta);
    }

    protected void postProcesamiento() {
        System.out.println("Venta completada: " + venta.getNumeroVenta());
    }

    protected void manejarError(Exception e) {
        System.err.println("Error en proceso de venta: " + e.getMessage());
        e.printStackTrace();
    }

    public Venta getVenta() {
        return venta;
    }
}
