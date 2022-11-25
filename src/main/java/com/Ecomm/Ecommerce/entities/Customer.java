package com.Ecomm.Ecommerce.entities;

import com.Ecomm.Ecommerce.utils.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter

public class Customer extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "Contact")
    private String contact;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "User_Id")
    @JsonBackReference(value = "customer")
    private User user;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL)
    @JsonManagedReference(value = "customerAddress")
    private List<Address> addresses = new ArrayList<>();

}