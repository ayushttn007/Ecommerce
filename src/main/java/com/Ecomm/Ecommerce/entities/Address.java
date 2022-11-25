package com.Ecomm.Ecommerce.entities;

import com.Ecomm.Ecommerce.utils.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;

@Entity
@Getter
@Setter
public class Address extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "City")
    private String city;

    @Column(name = "State")
    private String state;

    @Column(name = "Country")
    private String country;

    @Column(name = "Address_Line")
    private String addressLine;

    @Column(name = "Zip_Code")
    private String zipCode;

    @Column(name = "Label")
    private String label;

    @ManyToOne
    @JoinColumn(name = "Customer_ID")
    @JsonBackReference(value = "customerAddress")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Seller_ID")
    @JsonBackReference(value = "sellerAddress")
    private Seller seller;

}