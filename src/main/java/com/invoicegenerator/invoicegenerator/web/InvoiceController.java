package com.invoicegenerator.invoicegenerator.web;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.model.PrivatePurchaser;
import com.invoicegenerator.invoicegenerator.model.Users.CustomUserDetails;
import com.invoicegenerator.invoicegenerator.model.Users.User;
import com.invoicegenerator.invoicegenerator.model.Vendor;
import com.invoicegenerator.invoicegenerator.services.InvoiceService;
import com.invoicegenerator.invoicegenerator.services.PrivatePurchaserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @Autowired
    PrivatePurchaserService privatePurchaserService;

    @GetMapping("/listOfInvoices")
    public String viewInvoiceList(Model model){
        model.addAttribute("invoiceList", invoiceService.findAll());
        return "invoice/invoiceList";
    }

    @GetMapping("/getPrivatePurchasers")
    public String getPrivatePurchasers(Model model){
        Vendor vendor = new Vendor();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User customUser = (User) authentication.getPrincipal();
        vendor.setId(customUser.getId());
        PrivatePurchaser privatePurchaser = new PrivatePurchaser();
        privatePurchaser.setName("aa");
        vendor.getPrivatePurchaserList().add(privatePurchaser);

        model.addAttribute("vendorList",vendor.getPrivatePurchaserList());
        return "invoice/newInvoice";
    }

    @GetMapping("/showNewInvoiceForm")
    public String showNewInvoiceForm(Model model){
        Vendor vendor = new Vendor();
        Invoice invoice = new Invoice();

        model.addAttribute("invoice",invoice);
        model.addAttribute("vendor",vendor);
        invoice.setSavedPrivatePurchasers(vendor.getPrivatePurchaserList());
        return "invoice/newInvoice";
    }

    @PostMapping("/addNewInvoice")
    public String addNewInvoice(@ModelAttribute("invoice")Invoice invoice){
        invoiceService.saveInvoice(invoice);
        return "redirect:/listOfInvoices";
    }

    @PostMapping("/addNewPrivatePurchaser")
    public String addNewPrivatePurchaser(@ModelAttribute("privatePurchaser")PrivatePurchaser privatePurchaser){
        privatePurchaserService.save(privatePurchaser);
        return "redirect:/listOfInvoices";
    }

    @GetMapping("/invoice/showNewPrivatePurchaserForm")
    public String showNewPrivatePurchaserForm(Model model){
        PrivatePurchaser privatePurchaser = new PrivatePurchaser();
        model.addAttribute("privatePurchaser",privatePurchaser);
        return "invoice/newPurchaser";
    }


}
