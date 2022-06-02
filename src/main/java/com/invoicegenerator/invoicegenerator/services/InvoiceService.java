package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.Invoice;

import javax.persistence.criteria.CriteriaBuilder;

public interface InvoiceService {
    Invoice saveInvoice(Invoice invoice);
    void printInvoice(Invoice invoice);
    void deleteInvocieById(Long id);
    void deleteInvoice(Invoice invoice);
    void findAll();

}
