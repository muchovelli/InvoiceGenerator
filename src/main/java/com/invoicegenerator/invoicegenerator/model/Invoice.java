package com.invoicegenerator.invoicegenerator.model;

import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Currency;
import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Language;
import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor;

    @OneToOne
    @JoinColumn(name = "private_purchaser_id")
    private PrivatePurchaser privatePurchaser;

    @OneToOne
    @JoinColumn(name = "companyPurchaser_id")
    private CompanyPurchaser companyPurchaser;

    private String invoiceNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

    private String place;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate saleDate;

    @OneToMany
    private List<InvoiceEntry> invoiceEntries;

    private Float price;

    private String paymentType;

    private String dueDate;

    private Status status;

    private String amountPaid;

    private String notes;

    private Currency currency;

    private Language language;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate paymentDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sendTime;

    private String poNumber;

    private String privateNote;

    @OneToMany
    private List<PrivatePurchaser> savedPrivatePurchasers;
}
