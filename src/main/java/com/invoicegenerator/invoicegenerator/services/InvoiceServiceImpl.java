package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.model.InvoiceEntry;
import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Currency;
import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Vendor;
import com.invoicegenerator.invoicegenerator.repository.InvoiceRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.print.Doc;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    InvoiceRepository invoiceRepository;

    Font entriesFont = new Font(Font.FontFamily.HELVETICA, 6);
    Font entriesFontBold = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
    Font smallFont = new Font(Font.FontFamily.HELVETICA, 10);
    Font smallFontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);


    protected Map<Long, Invoice> map = new HashMap<>();
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);


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
        Document document = new Document(com.itextpdf.text.PageSize.A4);
        String fileName = "invoice_" + invoice.getId()+1 + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        addInvoiceDetails(document,invoice.getInvoiceNumber(),invoice.getSaleDate(),
                invoice.getPaymentDate(), invoice.getIssueDate(),invoice.getPaymentType());

        addBreak(document,5);

        addVendorSellerInfo(document,invoice.getVendor(),invoice.getPrivatePurchaser());

        addBreak(document,8);

        addItemsInfo(document, (ArrayList<InvoiceEntry>) invoice.getInvoiceEntries());

        addPaymentInfo(document,invoice.getCurrency(),invoice.getAmountPaid(),invoice.getPrice());

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
    public List<Invoice> findAll() {
        List<Invoice> invoicesList = invoiceRepository.findAll();
        return invoicesList;
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

    private void addInvoiceDetails(Document document,
                                   String invoiceNumber,
                                   LocalDate saleDate,
                                   LocalDate paymentDate,
                                   LocalDate issueDate,
                                   String paymentType) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{1, 1 });
        //table.setLockedWidth(true);
        document.open();

        //First row
        PdfPCell cell = new PdfPCell(new Phrase("Faktura numer: " + invoiceNumber,smallFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        table.addCell(cell);

        //empty row
        PdfPCell emptyCell = new PdfPCell(new Phrase(""));
        emptyCell.setFixedHeight(15);
        emptyCell.setBorder(Rectangle.NO_BORDER);
        emptyCell.setColspan(2);
        table.addCell(emptyCell);

        //First row
        PdfPCell cell2 = new PdfPCell(new Phrase("Data wystawienia: " + saleDate,smallFont));
        cell2.setFixedHeight(15);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setColspan(2);
        table.addCell(cell2);

        //First row
        PdfPCell cell5 = new PdfPCell(new Phrase("Data sprzedazy: " + paymentDate,smallFont));
        cell5.setFixedHeight(15);
        cell5.setBorder(Rectangle.NO_BORDER);
        cell5.setColspan(2);
        table.addCell(cell5);

        //First row
        PdfPCell cell3 = new PdfPCell(new Phrase("Termin platności: " + issueDate,smallFont));
        cell3.setFixedHeight(15);
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setColspan(2);
        table.addCell(cell3);

        //First row
        PdfPCell cell4 = new PdfPCell(new Phrase("Platnosc: " + paymentType,smallFont));
        cell4.setFixedHeight(15);
        cell4.setBorder(Rectangle.NO_BORDER);
        cell4.setColspan(2);
        table.addCell(cell4);



        document.add(table);
    }

    private void addVendorSellerInfo(Document document,
                                     Vendor vendor,
                                     PrivatePurchaser privatePurchaser) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 1,1});
        //table.setLockedWidth(true);
        //document.open();

        //First row
        PdfPCell cell = new PdfPCell(new Phrase("Sprzedawca", smallFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Kupujacy" ,smallFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        table.addCell(cell);

        addBreak(document,2);

        //First row
        cell = new PdfPCell(new Phrase(vendor.getName(), smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(privatePurchaser.getName() ,smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        table.addCell(cell);

        //Street row
        cell = new PdfPCell(new Phrase(vendor.getStreet(), smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(privatePurchaser.getStreet() ,smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        table.addCell(cell);

        //Address row
        cell = new PdfPCell(new Phrase(vendor.getPostCode() + " " + vendor.getCity() + ", " + vendor.getCountry(), smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase(privatePurchaser.getPostCode() + " " + privatePurchaser.getCity() + ", " + privatePurchaser.getCountry() ,smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        table.addCell(cell);

        //VatID row
        cell = new PdfPCell(new Phrase(vendor.getVatId(), smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setColspan(2);
        table.addCell(cell);

        //Contact row
        cell = new PdfPCell(new Phrase(vendor.getEmail(), smallFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
        cell.setColspan(2);


        document.add(table);
    }

    private void addItemsInfo(Document document, ArrayList<InvoiceEntry> invoiceEntryList) throws DocumentException {
        PdfPTable table = new PdfPTable(8);
        table.setTotalWidth(new float[]{ 1,6,2,1,1,3,3,3});

        //First row
        PdfPCell cell = new PdfPCell(new Phrase("LP", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Nazwa towaru / uslugi", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Kod Produktu", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Rabat", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Ilosc", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cena", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Cena po rabacie", entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Wartosc" ,entriesFontBold));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        cell.setColspan(2);
        table.addCell(cell);

        for(int i=0; i<invoiceEntryList.size();i++){
            cell = new PdfPCell(new Phrase(String.valueOf(i+1), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(invoiceEntryList.get(i).getName(), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(invoiceEntryList.get(i).getItemCode(), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(invoiceEntryList.get(i).getDiscount()), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(invoiceEntryList.get(i).getQuantity()), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(invoiceEntryList.get(i).getUnitNetPrice()), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            table.addCell(cell);

            float priceFinal = invoiceEntryList.get(i).getUnitNetPrice() - invoiceEntryList.get(i).getUnitNetPrice()*invoiceEntryList.get(i).getDiscount();

            cell = new PdfPCell(new Phrase(String.valueOf(priceFinal), entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(String.valueOf(priceFinal*invoiceEntryList.get(i).getQuantity()),entriesFont));
            cell.setFixedHeight(15);
            cell.setBorder(Rectangle.RECTANGLE);
            cell.setColspan(2);
            table.addCell(cell);
        }

        document.add(table);
    }

    private void addPaymentInfo(Document document, Currency currency, String amountPaid, Float price){

    }

    private void addBreak(Document document, int number) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 150, 150 });
        table.setLockedWidth(true);
        for(int i=0; i<number;i++){
            PdfPCell cell = new PdfPCell(new Phrase(""));
            cell.setFixedHeight(10);
            cell.setBorder(Rectangle.NO_BORDER);
            table.addCell(cell);
        }
        document.add(table);
    }
}
