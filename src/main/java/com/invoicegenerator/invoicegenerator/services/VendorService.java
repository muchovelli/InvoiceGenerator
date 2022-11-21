package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Vendor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface VendorService {

    void save(Vendor vendor, PrivatePurchaser privatePurchaser);
    PrivatePurchaser findById(Vendor vendor, Long id);
    List<PrivatePurchaser> findAll(Vendor vendor);
}
