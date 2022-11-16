package com.Ecomm.Ecommerce.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter


public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")

    private long id;

    @Column(name = "AUTHORITY")
    private String authority;


}