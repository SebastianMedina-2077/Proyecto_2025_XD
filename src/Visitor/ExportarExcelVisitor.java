package Visitor;

import Models.Venta;
import java.io.*;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ExportarExcelVisitor implements ReporteVisitor{
    private Workbook workbook;
    private Sheet sheet;
    private int rowNum = 0;

    public ExportarExcelVisitor() {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Ventas");
        crearEncabezado();
    }

    private void crearEncabezado() {
        Row headerRow = sheet.createRow(rowNum++);
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        String[] headers = {"Número Venta", "Cliente", "Fecha", "Subtotal",
                "Descuento", "IGV", "Total", "Estado"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    @Override
    public void visitarVenta(Venta venta) {
        Row row = sheet.createRow(rowNum++);
        row.createCell(0).setCellValue(venta.getNumeroVenta());
        row.createCell(1).setCellValue(venta.getCliente() != null ?
                venta.getCliente().getRazonSocial() : "");
        row.createCell(2).setCellValue(venta.getFechaVenta().toString());
        row.createCell(3).setCellValue(venta.getSubtotal());
        row.createCell(4).setCellValue(venta.getDescuentoTotal());
        row.createCell(5).setCellValue(venta.getIgv());
        row.createCell(6).setCellValue(venta.getTotal());
        row.createCell(7).setCellValue(venta.getEstado());
    }

    @Override
    public void visitarListaVentas(List<Venta> ventas) {
        for (Venta venta : ventas) {
            visitarVenta(venta);
        }

        // Ajustar ancho de columnas
        for (int i = 0; i < 8; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    public byte[] generarReporte() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        return baos.toByteArray();
    }
}
