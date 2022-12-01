package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.CategoryDto;
import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.Dto.MetaFieldValueDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CategoryResponseDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CategoryViewDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.MetaFieldValueResponseDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.SellerCategoryResponseDTO;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CategoryUpdateDto;
import com.Ecomm.Ecommerce.entities.Category;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryService {

    String saveMetaValue(CategoryMetaFieldDto categoryMetaFieldDto);

    List<CategoryMetaFieldDto> fetchMetafields();

    String saveCategory(CategoryDto categoryDto);

    public  String updateCategory(long categoryId,CategoryUpdateDto categoryDto);

    public CategoryViewDto fetchCategory(long id);

    public MetaFieldValueResponseDto saveMetaFieldValues(MetaFieldValueDto metaFieldValueDTO);

    List<CategoryResponseDto> fetchCategories(Pageable paging);

    List<SellerCategoryResponseDTO> fetchSellerCategory();

    Set<Category> fetchCustomerCategory(Optional<Integer> optionalId);
}
