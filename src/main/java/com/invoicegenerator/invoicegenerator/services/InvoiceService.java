package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Invoice;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface InvoiceService {
    Invoice saveInvoice(Invoice invoice);
    void printInvoice(Invoice invoice);
    void deleteInvocieById(Long id);
    void deleteInvoice(Invoice invoice);
    List<Invoice> findAll();

}
