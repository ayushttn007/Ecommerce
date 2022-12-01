package com.Ecomm.Ecommerce.repository;

import com.Ecomm.Ecommerce.entities.Category;
import com.Ecomm.Ecommerce.entities.CategoryMetadataFieldKey;
import com.Ecomm.Ecommerce.entities.CategoryMetadataFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMetadataFieldValueRepository extends JpaRepository<CategoryMetadataFieldValue, CategoryMetadataFieldKey> {
    List<CategoryMetadataFieldValue> findByCategory(Category category);
}