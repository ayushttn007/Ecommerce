package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.CategoryMetadataField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetaDataFieldRepo extends JpaRepository<CategoryMetadataField,Long> {

    public String findByName(String name);

}