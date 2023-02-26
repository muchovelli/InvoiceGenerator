package com.invoicegenerator.invoicegenerator.repository;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
