package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;

import java.util.List;

public interface CategoryService {

    String saveMetaValue(CategoryMetaFieldDto categoryMetaFieldDto);

    List<CategoryMetaFieldDto> fetchMetafields();
}
