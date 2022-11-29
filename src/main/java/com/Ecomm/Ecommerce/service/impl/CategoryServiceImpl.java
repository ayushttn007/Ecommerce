package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.CategoryDto;
import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.entities.Category;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserAlreadyExistsException;
import com.Ecomm.Ecommerce.repository.CategoryMetaDataFieldRepo;
import com.Ecomm.Ecommerce.repository.CategoryRepo;
import com.Ecomm.Ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;

    @Autowired  CategoryRepo categoryRepo;

   public  String saveMetaValue(CategoryMetaFieldDto categoryMetaFieldDto){
        String metaDataFieldValue = categoryMetaFieldDto.getName();
         String fieldValue = categoryMetaDataFieldRepo.findByName(metaDataFieldValue);
       CategoryMetadataField categoryMetadataFieldValue = new CategoryMetadataField();
         if(fieldValue != null) {
             throw new UserAlreadyExistsException("Meta Data field already Exists");
         }
            categoryMetadataFieldValue.setName(metaDataFieldValue);

        long fieldValueId =  categoryMetaDataFieldRepo.save(categoryMetadataFieldValue).getId();
        return "meta Data Id : " + fieldValueId;

    }

    public List<CategoryMetaFieldDto> fetchMetafields(){
        List<CategoryMetadataField> fieldList = categoryMetaDataFieldRepo.findAll();

        List<CategoryMetaFieldDto> responseFieldList = new ArrayList<>();

        fieldList.forEach(field -> {
            CategoryMetaFieldDto categoryMetaFieldDto = new CategoryMetaFieldDto();
            categoryMetaFieldDto.setId(field.getId());
            categoryMetaFieldDto.setName(field.getName());
            responseFieldList.add(categoryMetaFieldDto);
        });

        return responseFieldList;
    }

    public String saveCategory(CategoryDto categoryDto){
        Category category = new Category();

        category.setName(categoryDto.getName());
        if(categoryDto.getParentId() != null){
            Category parentCategory= categoryRepo.findById(categoryDto.getParentId())
                    .orElseThrow(()->new ResourceNotFoundException("No parent Category Found with this id"));
            category.setParent(parentCategory);
        }
        Long id=categoryRepo.save(category).getId();
        String response = "Category ID: " + id;
        return response;
    }



}