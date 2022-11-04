package com.invoicegenerator.invoicegenerator.repository;

import com.invoicegenerator.invoicegenerator.model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

}
