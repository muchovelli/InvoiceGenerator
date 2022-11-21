package com.invoicegenerator.invoicegenerator.InvoiceTest;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.model.InvoiceEntry;
import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Country;
import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Status;
import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Vendor;
import com.invoicegenerator.invoicegenerator.services.InvoiceService;
import com.invoicegenerator.invoicegenerator.services.InvoiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class InvoiceServiceTest {

    InvoiceServiceImpl invoiceServiceImpl = new InvoiceServiceImpl();

//    @Test
//    void saveInvoiceTest(){
//        Invoice invoice = new Invoice();
//        invoice.setId(12222L);
//        //invoice.setVendor(new Vendor(1L,"Twogggja stara","5534234234","Poznanska 12", "30-020","Krakow",Country.POLAND,"45241241241412","mBank", "email@email.com"));
//        invoice.setPrivatePurchaser(new PrivatePurchaser(2L,"A-client", 52421342, "32020","Warszawa","Krakowska", Country.POLAND,"dudaa@dupa.pl","aaaa.pl","+924242","+48752412421"));
//        invoice.setInvoiceNumber("n-01-01");
//        invoice.setPaymentDate(LocalDate.of(2022,07,20));
//        invoice.setIssueDate(LocalDate.now());
//        invoice.setPlace("place");
//        invoice.setSaleDate(LocalDate.of(2022,06,29));
//        List<InvoiceEntry> list = new ArrayList<>();
//        list.add(new InvoiceEntry(2L, "Gwozdzie","dup4-kod",0.05f, 500, "szt", 0.50f,"23","600","800"));
//        invoice.setInvoiceEntries(list);
//        invoice.setPaymentType("Karta");
//        invoice.setDueDate("1 day");
//        invoice.setStatus(Status.SENT);
//        invoice.setAmountPaid("500");
//        invoice.setSavedPrivatePurchasers(new ArrayList<>());
//        invoiceServiceImpl.printInvoice(invoice);
//
//    }
}
