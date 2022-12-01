package com.Ecomm.Ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMetadataFieldKey implements Serializable {
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_metadata_field_id")
    private Long categoryMetadataFieldId;
}