package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Vendor;
import com.invoicegenerator.invoicegenerator.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService{

    @Autowired
    VendorRepository vendorRepository;

    @Override
    public void save(Vendor vendor, PrivatePurchaser privatePurchaser) {
        vendorRepository.findById(vendor.getId());
        List<PrivatePurchaser> privatePurchasers = vendor.getPrivatePurchaserList();
        privatePurchasers.add(privatePurchaser);
        vendor.setPrivatePurchaserList(privatePurchasers);
        vendorRepository.save(vendor);
    }

    @Override
    public PrivatePurchaser findById(Vendor vendor, Long id) {
        return vendor.getPrivatePurchaserList().stream().filter(privatePurchaser -> privatePurchaser.getId().equals(id)).findFirst().get();
    }

    @Override
    public List<PrivatePurchaser> findAll(Vendor vendor) {
        return vendor.getPrivatePurchaserList();
    }
}
