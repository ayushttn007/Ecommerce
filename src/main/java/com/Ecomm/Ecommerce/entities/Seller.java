package com.Ecomm.Ecommerce.entities;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;

@Entity
@Getter
@Setter

public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "GST")
    private String gst;

    @Column(name = "Company_Contact")
    private String companyContact;

    @Column(name = "Company_Name")
    private String companyName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User_Id")
    @JsonBackReference
    private User user;

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Address address;
}