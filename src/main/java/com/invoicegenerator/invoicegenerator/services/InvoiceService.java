package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Vendor;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface InvoiceService {
    Invoice saveInvoice(Invoice invoice);
    void printInvoice(Invoice invoice);
    void deleteInvocieById(Long id);
    void deleteInvoice(Invoice invoice);
    List<Invoice> findAll();
    List<PrivatePurchaser> findAllPrivatePurchasers(Vendor vendor);
}
