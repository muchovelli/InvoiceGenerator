package com.invoicegenerator.invoicegenerator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CompanyPurchaser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String buyerFirstName;

    private String buyerLastName;

    private String street;

    private String postCode;

    private String City;
}
