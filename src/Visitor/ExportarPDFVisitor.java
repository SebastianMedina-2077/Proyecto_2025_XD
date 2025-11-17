package Visitor;

import Models.Venta;
import java.io.*;
import java.util.List;

import com.itextpdf.text.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ExportarPDFVisitor implements ReporteVisitor{
    private Document document;
    private ByteArrayOutputStream baos;
    private PdfPTable table;

    public ExportarPDFVisitor() {
        document = new Document(PageSize.A4.rotate());
        baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();

            // Título
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("REPORTE DE VENTAS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Crear tabla
            table = new PdfPTable(8);
            table.setWidthPercentage(100);

            // Encabezado
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
            String[] headers = {"N° Venta", "Cliente", "Fecha", "Subtotal",
                    "Descuento", "IGV", "Total", "Estado"};

            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitarVenta(Venta venta) {
        table.addCell(venta.getNumeroVenta());
        table.addCell(venta.getCliente() != null ?
                venta.getCliente().getRazonSocial() : "");
        table.addCell(venta.getFechaVenta().toString());
        table.addCell(String.format("S/ %.2f", venta.getSubtotal()));
        table.addCell(String.format("S/ %.2f", venta.getDescuentoTotal()));
        table.addCell(String.format("S/ %.2f", venta.getIgv()));
        table.addCell(String.format("S/ %.2f", venta.getTotal()));
        table.addCell(venta.getEstado());
    }

    @Override
    public void visitarListaVentas(List<Venta> ventas) {
        for (Venta venta : ventas) {
            visitarVenta(venta);
        }
    }

    @Override
    public byte[] generarReporte() throws Exception {
        document.add(table);
        document.close();
        return baos.toByteArray();
    }
}
