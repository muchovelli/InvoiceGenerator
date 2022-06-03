package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.repository.InvoiceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    InvoiceRepository invoiceRepository;

    protected Map<Long, Invoice> map = new HashMap<>();


    @Override
    public Invoice saveInvoice(Invoice invoice) {
        if(invoice == null) return null;

        if(invoice.getId() == null) invoice.setId(getNextId());

        if(!verifyInvoice(invoice)) return null;

        map.put(invoice.getId(),invoice);
        invoiceRepository.save(invoice);
        return invoice;
    }

    @SneakyThrows
    @Override
    public void printInvoice(Invoice invoice) {
        Document document = new Document();
        String fileName = "invoice_" + invoice.getId() + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        PdfPTable table = new PdfPTable(3); // 3 columns.
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table

        //Set Column widths
        float[] columnWidths = {1f, 1f, 1f};
        table.setWidths(columnWidths);

        PdfPCell cell1 = new PdfPCell(new Paragraph("Sprzedawca"));
        cell1.setBorderColor(BaseColor.WHITE);
        cell1.setPaddingLeft(10);
        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPCell cell2 = new PdfPCell();
        cell2.setBorderColor(BaseColor.WHITE);


        PdfPCell cell3 = new PdfPCell();
        cell3.addElement(new Paragraph("Nabywca"));

        cell3.setBorderColor(BaseColor.WHITE);
        cell3.setPaddingLeft(10);
        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell3.addElement(new Paragraph(String.valueOf(invoice.getPrivatePurchaser().getEmail())));




        Chunk chunk = new Chunk(String.valueOf(invoice.getId()), font);
        Chunk chunk1 = new Chunk(String.valueOf(invoice.getInvoiceNumber()), font);
        table.addCell(cell1);
        table.addCell(cell2);
        table.addCell(cell3);



        try {
            document.add(chunk);
            document.add(chunk1);
            document.add(table);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }



    @Override
    public void deleteInvocieById(Long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public void deleteInvoice(Invoice invoice) {
        invoiceRepository.delete(invoice);
    }

    @Override
    public void findAll() {
        invoiceRepository.findAll();
    }

    private Long getNextId(){
        Long nextId = null;

        try {
            nextId = Collections.max(map.keySet()) + 1;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }

    private boolean verifyInvoice(Invoice invoice){
        if(invoice.getPrivatePurchaser() == null
                && invoice.getCompanyPurchaser() == null) return false;

        if(invoice.getVendor() == null) return false;

        return true;
    }


}
