package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.CategoryDto;
import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.Dto.ChildCategoryDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CategoryViewDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CategoryUpdateDto;
import com.Ecomm.Ecommerce.entities.Category;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;
import com.Ecomm.Ecommerce.handler.InvalidException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserAlreadyExistsException;
import com.Ecomm.Ecommerce.repository.CategoryMetaDataFieldRepo;
import com.Ecomm.Ecommerce.repository.CategoryRepo;
import com.Ecomm.Ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMetaDataFieldRepo categoryMetaDataFieldRepo;

    @Autowired  CategoryRepo categoryRepo;

    @Autowired
    MessageSource messageSource;

   public  String saveMetaValue(CategoryMetaFieldDto categoryMetaFieldDto){
        String metaDataFieldValue = categoryMetaFieldDto.getName();
         String fieldValue = categoryMetaDataFieldRepo.findByName(metaDataFieldValue);
       CategoryMetadataField categoryMetadataFieldValue = new CategoryMetadataField();
         if(fieldValue != null) {
             // make custom exception
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
                    .orElseThrow(()->new ResourceNotFoundException(
                            messageSource.getMessage("api.error.IdNotFound",null, Locale.ENGLISH)
                    ));
            category.setParent(parentCategory);
        }
        Long id=categoryRepo.save(category).getId();
        String response = "Category ID: " + id;
        return response;
    }

   public  String updateCategory(long categoryId,CategoryUpdateDto categoryDto){
       Category category = categoryRepo.findById(categoryId).orElseThrow(
               () ->  new ResourceNotFoundException(
                       messageSource.getMessage("api.error.IdNotFound",null, Locale.ENGLISH)
               )
       );

       category.setName(categoryDto.getName());
       categoryRepo.save(category);
       return messageSource.getMessage("api.response.categoryUpdate",null,Locale.ENGLISH);
   }


//    Method to return categories
    public CategoryViewDto fetchCategory(long id) {
        Category category = categoryRepo.findById(id).orElseThrow(() -> new InvalidException(
                messageSource.getMessage("api.error.IdNotFound", null, Locale.ENGLISH)
        ));
        CategoryViewDto categoryViewDto = new CategoryViewDto();
        categoryViewDto.setId(category.getId());
        categoryViewDto.setName(category.getName());
        categoryViewDto.setParent(category.getParent());


        Set<ChildCategoryDto> childList = new HashSet<>();

        for(Category child: category.getChildren()){
            ChildCategoryDto childCategoryDto = new ChildCategoryDto();
            childCategoryDto.setId(child.getId());
            childCategoryDto.setName(child.getName());
            childList.add(childCategoryDto);

        }
        categoryViewDto.setChildren(childList);
        return categoryViewDto;

    }

}