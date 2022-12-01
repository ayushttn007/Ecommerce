package com.Ecomm.Ecommerce.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "product_gen")
    @SequenceGenerator(name="product_gen", sequenceName = "product_seq", initialValue = 1, allocationSize = 1)
    private long id;


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private String name;

    private String description;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private String brand;

    private boolean isActive;
    private boolean isDeleted;

}
