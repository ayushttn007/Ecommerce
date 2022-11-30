package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.CategoryDto;
import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CategoryViewDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CategoryUpdateDto;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;

import java.util.List;

public interface CategoryService {

    String saveMetaValue(CategoryMetaFieldDto categoryMetaFieldDto);

    List<CategoryMetaFieldDto> fetchMetafields();

    String saveCategory(CategoryDto categoryDto);

    public  String updateCategory(long categoryId,CategoryUpdateDto categoryDto);

    public CategoryViewDto fetchCategory(long id);
}
