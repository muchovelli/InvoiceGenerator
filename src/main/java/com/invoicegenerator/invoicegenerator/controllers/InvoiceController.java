package com.invoicegenerator.invoicegenerator.controllers;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Vendor;
import com.invoicegenerator.invoicegenerator.services.InvoiceService;
import com.invoicegenerator.invoicegenerator.services.PrivatePurchaserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceController {

    InvoiceService invoiceService;

    PrivatePurchaserService privatePurchaserService;

    @GetMapping("/listOfInvoices")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR') or hasRole('ROLE_ADMIN')")
    public String viewInvoiceList(Model model) {
        model.addAttribute("invoiceList", invoiceService.findAll());
        return "invoice/invoiceList";
    }

    @GetMapping("/getPrivatePurchasers")
    public String getPrivatePurchasers(Model model) {
        Vendor vendor = new Vendor();
        PrivatePurchaser privatePurchaser = new PrivatePurchaser();
        privatePurchaser.setName("aa");
        vendor.getPrivatePurchaserList().add(privatePurchaser);

        model.addAttribute("vendorList", vendor.getPrivatePurchaserList());
        return "invoice/newInvoice";
    }

    @GetMapping("/showNewInvoiceForm")
    public String showNewInvoiceForm(Model model) {
        Vendor vendor = new Vendor();
        Invoice invoice = new Invoice();

        model.addAttribute("invoice", invoice);
        model.addAttribute("vendor", vendor);
        invoice.setSavedPrivatePurchasers(vendor.getPrivatePurchaserList());
        return "invoice/newInvoice";
    }

    @PostMapping("/addNewInvoice")
    public String addNewInvoice(@ModelAttribute("invoice") Invoice invoice) {
        invoiceService.saveInvoice(invoice);
        return "redirect:/listOfInvoices";
    }

    @PostMapping("/addNewPrivatePurchaser")
    public String addNewPrivatePurchaser(@ModelAttribute("privatePurchaser") PrivatePurchaser privatePurchaser) {
        privatePurchaserService.save(privatePurchaser);
        return "redirect:/listOfInvoices";
    }

    @GetMapping("/invoice/showNewPrivatePurchaserForm")
    public String showNewPrivatePurchaserForm(Model model) {
        PrivatePurchaser privatePurchaser = new PrivatePurchaser();
        model.addAttribute("privatePurchaser", privatePurchaser);
        return "invoice/newPurchaser";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return "redirect:/error";
    }


}
