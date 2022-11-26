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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService{

    @Autowired
    InvoiceRepository invoiceRepository;

    Font entriesFont = new Font(Font.FontFamily.HELVETICA, 6);
    Font entriesFontBold = new Font(Font.FontFamily.HELVETICA, 6, Font.BOLD);
    Font smallFont = new Font(Font.FontFamily.HELVETICA, 10);
    Font smallFontBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);


    protected Map<Long, Invoice> map = new HashMap<>();
    private static final Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static final Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);

    private float finalCost = 0.0f;

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
        //Invoice document configuration
        Document document = new Document(com.itextpdf.text.PageSize.A4);
        String fileName = "invoice_" + invoice.getId()+1 + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }

        addInvoiceDetails(document,invoice.getInvoiceNumber(),invoice.getSaleDate(),
                invoice.getPaymentDate(), invoice.getIssueDate(),invoice.getPaymentType());

        addBreak(document,5);

        addVendorSellerInfo(document,invoice.getVendor(),invoice.getPrivatePurchaser());

        addBreak(document,8);

        addBottomBorder(document);

        addItemsInfo(document, (ArrayList<InvoiceEntry>) invoice.getInvoiceEntries());

        addPaymentInfo(invoice.getCurrency(),invoice.getAmountPaid(),invoice.getPrice());

        addBreak(document,1);

        addFinalCost(document);

        addBreak(document,5);

        addSignature(document);

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
        return invoiceRepository.findAll();
    }

    @Override
    public List<PrivatePurchaser> findAllPrivatePurchasers(Vendor vendor) {
        return vendor.getPrivatePurchaserList();
    }

    private Long getNextId(){
        long nextId;
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

        return invoice.getVendor() != null;
    }

    private void addInvoiceDetails(Document document,
                                   String invoiceNumber,
                                   LocalDate saleDate,
                                   LocalDate paymentDate,
                                   LocalDate issueDate,
                                   String paymentType) throws DocumentException {

        //Invoice details view configuration
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(new float[]{1});
        document.open();

        //Invoice number row
        table.addCell(addBorderlessCell("Faktura numer: " + invoiceNumber,false, 15,true));

        //empty row
        addBreak(document, 1);

        //Sale date row
        table.addCell(addBorderlessCell("Data wystawienia: " + saleDate,false, 15,false));

        //Payment due row
        table.addCell(addBorderlessCell("Data sprzedazy: " + paymentDate,false, 15,false));

        //Issue date row
        table.addCell(addBorderlessCell("Termin platnosci: " + issueDate,false, 15,false));

        //Payment type row
        table.addCell(addBorderlessCell("Platnosc: " + paymentType,false, 15,false));

        document.add(table);
    }

    private void addVendorSellerInfo(Document document,
                                     Vendor vendor,
                                     PrivatePurchaser privatePurchaser) throws DocumentException {

        //Vendor and Seller view configuration
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 1,1});


        //First row
        table.addCell(addBorderlessCell("Sprzedawca",false, 15,true));
        table.addCell(addBorderlessCell("Kupujący",false, 15,true));

        addBreak(document,2);

        //Name row
        table.addCell(addBorderlessCell(vendor.getName(),false, 15,false));
        table.addCell(addBorderlessCell(privatePurchaser.getName(),false, 15,false));


        //Street row
        table.addCell(addBorderlessCell(vendor.getStreet(),false, 15,false));
        table.addCell(addBorderlessCell(privatePurchaser.getStreet(),false, 15,false));


        //Address row
        String vendorAddress = vendor.getPostCode() + " " + vendor.getCity() + ", " + vendor.getCountry();
        table.addCell(addBorderlessCell(vendorAddress,false, 15,false));

        String purchaserAddress = privatePurchaser.getPostCode() + " " + privatePurchaser.getCity() + ", " + privatePurchaser.getCountry();
        table.addCell(addBorderlessCell(purchaserAddress,false, 15,false));

        //VatID row
        table.addCell(addBorderlessCell(vendor.getVatId(),false, 15,false));

        //Contact row
        table.addCell(addBorderlessCell(vendor.getEmail(),false, 15,false));

        document.add(table);
    }

    private void addItemsInfo(Document document, ArrayList<InvoiceEntry> invoiceEntryList) throws DocumentException {
        //Entry list view configuration
        PdfPTable table = new PdfPTable(8);
        table.setTotalWidth(new float[]{ 1,6,2,1,1,3,3,3});

        //Title row
        table.addCell(addItemsTitleCell("LP"));
        table.addCell(addItemsTitleCell("Nazwa towaru / uslugi"));
        table.addCell(addItemsTitleCell("Kod"));
        table.addCell(addItemsTitleCell("Rabat"));
        table.addCell(addItemsTitleCell("Ilosc"));
        table.addCell(addItemsTitleCell("Cena "));
        table.addCell(addItemsTitleCell("Cena po rabacie"));
        table.addCell(addItemsTitleCell("Wartosc"));


        //Items row
        for(int i=0; i<invoiceEntryList.size();++i){
            //LP
            table.addCell(addEntryCell(String.valueOf(i+1),true));
            //Item name
            table.addCell(addEntryCell(invoiceEntryList.get(i).getName(),true));
            //Item code
            table.addCell(addEntryCell(invoiceEntryList.get(i).getItemCode(),true));
            //Discount
            table.addCell(addEntryCell(String.valueOf(invoiceEntryList.get(i).getDiscount()),true));
            //Quantity
            table.addCell(addEntryCell(String.valueOf(invoiceEntryList.get(i).getQuantity()),true));
            //Price
            table.addCell(addEntryCell(String.valueOf(invoiceEntryList.get(i).getUnitNetPrice()),true));
            //Price after discount
            float priceAfterDiscount = invoiceEntryList.get(i).getUnitNetPrice() - invoiceEntryList.get(i).getUnitNetPrice()*invoiceEntryList.get(i).getDiscount();
            table.addCell(addEntryCell(String.valueOf(priceAfterDiscount),true));
            //Value
            table.addCell(addEntryCell(String.valueOf(priceAfterDiscount*invoiceEntryList.get(i).getQuantity()),true));

            finalCost = finalCost+priceAfterDiscount*invoiceEntryList.get(i).getQuantity();
        }

        document.add(table);
    }

    private void addPaymentInfo(Currency currency, String amountPaid, Float price){

    }

    private void addBreak(Document document, int number) throws DocumentException {
        //Break view configuration
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 150, 150 });
        table.setLockedWidth(true);

        for(int i=0; i<number;i++){
            table.addCell(addBorderlessCell(" ",false,10,false));
        }
        document.add(table);
    }

    private void addSignature(Document document) throws DocumentException {
        //View configuration
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 250, 150 });
        table.setLockedWidth(true);

        table.addCell(addBorderlessCell("Podpis wystawiajacego",true,15,true));
        table.addCell(addBorderlessCell("Podpis kupujacego",false, 15,true));

        document.add(table);
    }

    private void addFinalCost(Document document) throws DocumentException{
        //View configuration
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(new float[]{ 250, 150 });
        table.setLockedWidth(true);

        table.addCell(addBorderlessCell(" ",false, 15,false));
        table.addCell(addBorderlessCell("Razem do zaplaty: " + finalCost,false, 15,true));

        document.add(table);
    }

    private void addBottomBorder(Document document) throws DocumentException{
        //View configuration
        PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(new float[]{418});
        table.setLockedWidth(true);
        PdfPCell cell = new PdfPCell(new Phrase(" ", entriesFontBold));
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.BOTTOM);
        table.addCell(cell);

        document.add(table);
    }

    private PdfPCell addItemsTitleCell(String name){
        PdfPCell cell = new PdfPCell(new Phrase(name, entriesFontBold));
        cell.setFixedHeight(10);
        cell.setBorder(Rectangle.RECTANGLE);

        cell.setBackgroundColor(new BaseColor(178, 178, 178));
        cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

        return cell;
    }

    private PdfPCell addEntryCell(String text, boolean isLeftAligned){
        PdfPCell cell = new PdfPCell(new Phrase((text), entriesFont));
        cell.setFixedHeight(15);
        cell.setBorder(Rectangle.RECTANGLE);
        if(isLeftAligned) cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }

    private PdfPCell addBorderlessCell(String text, boolean isLeftAligned, int fixedHeight, boolean isBold){
        PdfPCell cell = new PdfPCell(new Phrase((text), smallFont));
        cell.setFixedHeight(fixedHeight);
        cell.setBorder(Rectangle.NO_BORDER);
        if(isBold) cell.setPhrase(new Phrase((text), smallFontBold));
        if(isLeftAligned) cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        return cell;
    }
}
