package com.invoicegenerator.invoicegenerator.InvoiceTest;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.services.InvoiceService;
import com.invoicegenerator.invoicegenerator.services.InvoiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class InvoiceServiceTest {

    InvoiceServiceImpl invoiceServiceImpl = new InvoiceServiceImpl();

    @Test
    void saveInvoiceTest(){
        Invoice invoice = new Invoice();
        invoice.setId(12222L);
        invoiceServiceImpl.printInvoice(invoice);
    }
}
