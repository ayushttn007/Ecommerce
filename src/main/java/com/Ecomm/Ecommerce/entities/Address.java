package com.Ecomm.Ecommerce.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Address {
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
    private long zipCode;

    @Column(name = "Label")
    private String label;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Customer_ID")
    private Customer customer;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Seller_ID")
    private Seller seller;

}