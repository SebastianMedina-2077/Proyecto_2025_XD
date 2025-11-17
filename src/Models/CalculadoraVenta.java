package Models;

import Strategy.EstrategiaDescuento;
import Strategy.EstrategiaIGV;
import Strategy.IGVEstandar;
import Strategy.SinDescuento;

import java.util.List;

public class CalculadoraVenta {
    private EstrategiaDescuento estrategiaDescuento;
    private EstrategiaIGV estrategiaIGV;

    public CalculadoraVenta() {
        this.estrategiaDescuento = new SinDescuento();
        this.estrategiaIGV = new IGVEstandar();
    }

    public void setEstrategiaDescuento(EstrategiaDescuento estrategia) {
        this.estrategiaDescuento = estrategia;
    }

    public void setEstrategiaIGV(EstrategiaIGV estrategia) {
        this.estrategiaIGV = estrategia;
    }

    public ResultadoCalculo calcular(double subtotal, Cliente cliente, List<DetalleVenta> detalles) {
        double descuento = estrategiaDescuento.calcularDescuento(subtotal, cliente, detalles);
        double baseImponible = subtotal - descuento;
        double igv = estrategiaIGV.calcularIGV(baseImponible);
        double total = baseImponible + igv;

        return new ResultadoCalculo(subtotal, descuento, baseImponible, igv, total,
                estrategiaDescuento.getDescripcion(), estrategiaIGV.getDescripcion());
    }

    public static class ResultadoCalculo {
        private double subtotal;
        private double descuento;
        private double baseImponible;
        private double igv;
        private double total;
        private String descripcionDescuento;
        private String descripcionIGV;

        public ResultadoCalculo(double subtotal, double descuento, double baseImponible,
                                double igv, double total, String descDesc, String descIGV) {
            this.subtotal = subtotal;
            this.descuento = descuento;
            this.baseImponible = baseImponible;
            this.igv = igv;
            this.total = total;
            this.descripcionDescuento = descDesc;
            this.descripcionIGV = descIGV;
        }

        public double getSubtotal() { return subtotal; }
        public double getDescuento() { return descuento; }
        public double getBaseImponible() { return baseImponible; }
        public double getIgv() { return igv; }
        public double getTotal() { return total; }
        public String getDescripcionDescuento() { return descripcionDescuento; }
        public String getDescripcionIGV() { return descripcionIGV; }

        @Override
        public String toString() {
            return String.format(
                    "Subtotal: S/ %.2f\n" +
                            "Descuento (%s): S/ %.2f\n" +
                            "Base Imponible: S/ %.2f\n" +
                            "%s: S/ %.2f\n" +
                            "TOTAL: S/ %.2f",
                    subtotal, descripcionDescuento, descuento, baseImponible,
                    descripcionIGV, igv, total
            );
        }
    }
}
