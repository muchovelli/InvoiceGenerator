package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;

import java.util.List;

public interface PrivatePurchaserService {
    PrivatePurchaser save(PrivatePurchaser privatePurchaser);
    PrivatePurchaser findById(Long id);
    List<PrivatePurchaser> findAll();
}
