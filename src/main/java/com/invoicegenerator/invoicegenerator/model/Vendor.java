package com.invoicegenerator.invoicegenerator.model;

import com.invoicegenerator.invoicegenerator.model.InvoiceEnums.Country;
import com.invoicegenerator.invoicegenerator.model.Users.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vendor extends Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String vatId;

    private String street;

    private String postCode;

    private String city;

    private Country country;

    private String bankAccount;

    private String bankName;

    private String email;

    @OneToMany
    private List<PrivatePurchaser> privatePurchaserList;

}
