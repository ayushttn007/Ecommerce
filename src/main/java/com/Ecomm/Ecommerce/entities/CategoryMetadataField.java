package com.Ecomm.Ecommerce.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CategoryMetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "metadata_gen")
    @SequenceGenerator(name="metadata_gen", sequenceName = "metadata_seq", initialValue = 1, allocationSize = 1)
    private long id;

    private String name;
}