package com.invoicegenerator.invoicegenerator.services;

import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.TypeGUS;
import com.invoicegenerator.invoicegenerator.model.Vendor;

public interface VendorInterface {
    void saveVendor(Vendor vendor);
    void autofillVendor(String number,TypeGUS typeGUS);
}
