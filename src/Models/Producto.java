package Models;

import java.time.LocalDateTime;

public class Producto {
    private int idProducto;
    private String codigoProducto;
    private String nombreProducto;
    private String descripcion;
    private int idCategoria;
    private String nombreCategoria;
    private int idMarca;
    private String nombreMarca;
    private String edadRecomendada;
    private double precioUnidad;
    private double precioDocena;
    private double precioMayorista;
    private int cantidadMinimaMayorista;
    private int stockMinimo;
    private double pesoKg;
    private LocalDateTime fechaCreacion;
    private String estado;

    private int stockTotal;
    private int stock251;
    private int stock252;

    private Producto(ProductoBuilder builder) {
        this.codigoProducto = builder.codigoProducto;
        this.nombreProducto = builder.nombreProducto;
        this.descripcion = builder.descripcion;
        this.idCategoria = builder.idCategoria;
        this.idMarca = builder.idMarca;
        this.edadRecomendada = builder.edadRecomendada;
        this.precioUnidad = builder.precioUnidad;
        this.precioDocena = builder.precioDocena;
        this.precioMayorista = builder.precioMayorista;
        this.cantidadMinimaMayorista = builder.cantidadMinimaMayorista;
        this.stockMinimo = builder.stockMinimo;
        this.pesoKg = builder.pesoKg;
        this.estado = "Activo";
    }

    public Producto() {
    }

    public static class ProductoBuilder {
        private String codigoProducto;
        private String nombreProducto;
        private String descripcion;
        private int idCategoria;
        private int idMarca;
        private String edadRecomendada;
        private double precioUnidad;
        private double precioDocena;
        private double precioMayorista;
        private int cantidadMinimaMayorista = 50;
        private int stockMinimo = 10;
        private double pesoKg;

        public ProductoBuilder(String codigoProducto, String nombreProducto) {
            this.codigoProducto = codigoProducto;
            this.nombreProducto = nombreProducto;
        }

        public ProductoBuilder descripcion(String descripcion) {
            this.descripcion = descripcion;
            return this;
        }

        public ProductoBuilder categoria(int idCategoria) {
            this.idCategoria = idCategoria;
            return this;
        }

        public ProductoBuilder marca(int idMarca) {
            this.idMarca = idMarca;
            return this;
        }

        public ProductoBuilder edadRecomendada(String edad) {
            this.edadRecomendada = edad;
            return this;
        }

        public ProductoBuilder precioUnidad(double precio) {
            this.precioUnidad = precio;
            return this;
        }

        public ProductoBuilder precioDocena(double precio) {
            this.precioDocena = precio;
            return this;
        }

        public ProductoBuilder precioMayorista(double precio) {
            this.precioMayorista = precio;
            return this;
        }

        public ProductoBuilder precios(double unidad, double docena, double mayorista) {
            this.precioUnidad = unidad;
            this.precioDocena = docena;
            this.precioMayorista = mayorista;
            return this;
        }

        public ProductoBuilder cantidadMinimaMayorista(int cantidad) {
            this.cantidadMinimaMayorista = cantidad;
            return this;
        }

        public ProductoBuilder stockMinimo(int stock) {
            this.stockMinimo = stock;
            return this;
        }

        public ProductoBuilder peso(double peso) {
            this.pesoKg = peso;
            return this;
        }

        public Producto build() {
            validar();
            return new Producto(this);
        }

        private void validar() {
            if (codigoProducto == null || codigoProducto.trim().isEmpty()) {
                throw new IllegalStateException("El codigo de producto es obligatorio");
            }
            if (nombreProducto == null || nombreProducto.trim().isEmpty()) {
                throw new IllegalStateException("El nombre del producto es obligatorio");
            }
            if (idCategoria <= 0) {
                throw new IllegalStateException("Debe seleccionar una categoria");
            }
            if (idMarca <= 0) {
                throw new IllegalStateException("Debe seleccionar una marca");
            }
            if (precioUnidad <= 0) {
                throw new IllegalStateException("El precio por unidad debe ser mayor a 0");
            }
            if (precioDocena <= 0) {
                throw new IllegalStateException("El precio por docena debe ser mayor a 0");
            }
            if (precioMayorista <= 0) {
                throw new IllegalStateException("El precio mayorista debe ser mayor a 0");
            }
        }
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getEdadRecomendada() {
        return edadRecomendada;
    }

    public void setEdadRecomendada(String edadRecomendada) {
        this.edadRecomendada = edadRecomendada;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public double getPrecioMayorista() {
        return precioMayorista;
    }

    public void setPrecioMayorista(double precioMayorista) {
        this.precioMayorista = precioMayorista;
    }

    public int getCantidadMinimaMayorista() {
        return cantidadMinimaMayorista;
    }

    public void setCantidadMinimaMayorista(int cantidadMinimaMayorista) {
        this.cantidadMinimaMayorista = cantidadMinimaMayorista;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getStockTotal() {
        return stockTotal;
    }

    public void setStockTotal(int stockTotal) {
        this.stockTotal = stockTotal;
    }

    public int getStock251() {
        return stock251;
    }

    public void setStock251(int stock251) {
        this.stock251 = stock251;
    }

    public int getStock252() {
        return stock252;
    }

    public void setStock252(int stock252) {
        this.stock252 = stock252;
    }

    @Override
    public String toString() {
        return nombreProducto + " (" + codigoProducto + ")";
    }
}
