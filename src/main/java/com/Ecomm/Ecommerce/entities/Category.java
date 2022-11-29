package com.Ecomm.Ecommerce.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Category {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "category_gen")
   @SequenceGenerator(name="category_gen", sequenceName = "category_seq", initialValue = 1, allocationSize = 1)
    private long id;

    private String name;

    @OneToOne
    @JoinColumn(name = "parent_Id")
    private Category parent;

    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    private Set<Category>children = new HashSet<>();
}