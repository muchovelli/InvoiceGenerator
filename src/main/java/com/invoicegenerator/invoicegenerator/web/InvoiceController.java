package com.invoicegenerator.invoicegenerator.web;

import com.invoicegenerator.invoicegenerator.model.Invoice;
import com.invoicegenerator.invoicegenerator.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/listOfInvoices")
    public String viewInvoiceList(Model model){
        model.addAttribute("invoiceList", invoiceService.findAll());
        return "invoice/invoiceList";
    }

    @GetMapping("/showNewInvoiceForm")
    public String showNewInvoiceForm(Model model){
        Invoice invoice = new Invoice();
        model.addAttribute("invoice",invoice);
        return "invoice/newInvoice";
    }

    @PostMapping("/addNewInvoice")
    public String addNewInvoice(@ModelAttribute("invoice")Invoice invoice){
        invoiceService.saveInvoice(invoice);
        return "redirect:/";
    }
}
