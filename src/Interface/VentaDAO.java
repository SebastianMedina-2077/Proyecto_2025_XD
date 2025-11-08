package Interface;

import Clases_Tienda.VentaCabecera;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import Enum.EstadoVenta;

public interface VentaDAO extends GenericDAO<VentaCabecera, String> {
    List<VentaCabecera> listarPorFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<VentaCabecera> listarPorCliente(String idCliente);
    List<VentaCabecera> listarPorVendedor(Integer idVendedor);
    List<VentaCabecera> listarPorEstado(EstadoVenta estado);
    BigDecimal calcularTotalVentasPorPeriodo(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
