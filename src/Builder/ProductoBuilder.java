package Builder;

import Catalogos.Estado;
import Clases.Proveedor;
import Clases_Tienda.Categoria;
import Clases_Tienda.Marca;
import Clases_Tienda.Producto;
import java.math.BigDecimal;

import java.time.LocalDate;

public class ProductoBuilder {
    private Producto producto = new Producto();

    public ProductoBuilder codigo(String codigo) {
        producto.setCodigoProducto(codigo);
        return this;
    }

    public ProductoBuilder nombre(String nombre) {
        producto.setNombre(nombre);
        return this;
    }

    public ProductoBuilder descripcion(String descripcion) {
        producto.setDescripcion(descripcion);
        return this;
    }

    public ProductoBuilder precioCompra(BigDecimal precio) {
        producto.setPrecioCompra(precio);
        return this;
    }

    public ProductoBuilder precioVenta(BigDecimal precio) {
        producto.setPrecioVenta(precio);
        return this;
    }

    public ProductoBuilder stockMinimo(int stock) {
        producto.setStockMinimo(stock);
        return this;
    }

    public ProductoBuilder lote(String lote) {
        producto.setLote(lote);
        return this;
    }

    public ProductoBuilder fechaVencimiento(LocalDate fecha) {
        producto.setFechaVencimiento(fecha);
        return this;
    }

    public ProductoBuilder categoria(Categoria categoria) {
        producto.setCategoria(categoria);
        return this;
    }

    public ProductoBuilder marca(Marca marca) {
        producto.setMarca(marca);
        return this;
    }

    public ProductoBuilder proveedor(Proveedor proveedor) {
        producto.setProveedor(proveedor);
        return this;
    }

    public ProductoBuilder estado(Estado estado) {
        producto.setEstado(estado);
        return this;
    }

    public Producto construir() {
        if (producto.getCodigoProducto() == null || producto.getNombre() == null ||
                producto.getPrecioCompra() == null || producto.getPrecioVenta() == null ||
                producto.getCategoria() == null || producto.getMarca() == null ||
                producto.getProveedor() == null) {
            throw new IllegalStateException("Faltan campos obligatorios para construir el producto");
        }
        return producto;
    }
}
