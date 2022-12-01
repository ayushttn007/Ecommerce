package com.Ecomm.Ecommerce.service.impl;

import com.Ecomm.Ecommerce.Dto.CategoryDto;
import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.Dto.ChildCategoryDto;
import com.Ecomm.Ecommerce.Dto.MetaFieldValueDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.*;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CategoryUpdateDto;
import com.Ecomm.Ecommerce.entities.Category;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;
import com.Ecomm.Ecommerce.entities.CategoryMetadataFieldKey;
import com.Ecomm.Ecommerce.entities.CategoryMetadataFieldValue;
import com.Ecomm.Ecommerce.handler.InvalidException;
import com.Ecomm.Ecommerce.handler.ResourceNotFoundException;
import com.Ecomm.Ecommerce.handler.UserAlreadyExistsException;
import com.Ecomm.Ecommerce.repository.CategoryMetaDataFieldRepo;
import com.Ecomm.Ecommerce.repository.CategoryMetadataFieldValueRepository;
import com.Ecomm.Ecommerce.repository.CategoryRepo;
import com.Ecomm.Ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    CategoryMetadataFieldValueRepository categoryMetadataFieldValueRepository;

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

    public MetaFieldValueResponseDto saveMetaFieldValues(MetaFieldValueDto metaFieldValueDTO){

        Long categoryId = metaFieldValueDTO.getCategoryId();
        Long metadataId = metaFieldValueDTO.getMetadataId();
        Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new InvalidException(
                messageSource.getMessage("api.error.IdNotFound", null, Locale.ENGLISH)
        ));
        CategoryMetadataField metaField = categoryMetaDataFieldRepo.findById(metadataId).orElseThrow(() -> new InvalidException(
                messageSource.getMessage("api.error.IdNotFound", null, Locale.ENGLISH)
        ));

        CategoryMetadataFieldValue fieldValue = new CategoryMetadataFieldValue();
        fieldValue.setCategoryMetadataFieldKey(new CategoryMetadataFieldKey(category.getId(), metaField.getId()));
        fieldValue.setCategory(category);
        fieldValue.setCategoryMetadataField(metaField);

        String newValues = "";

        CategoryMetadataFieldKey key = new CategoryMetadataFieldKey(categoryId,metadataId);
        Optional<CategoryMetadataFieldValue> object = categoryMetadataFieldValueRepository.findById(key);
        String originalValues="";
        if(object.isPresent()){
            originalValues = object.get().getValue();
        }

        if(originalValues!=null){
            newValues = originalValues;
        }

        Optional<List<String>> check = Optional.of(List.of(originalValues.split(",")));



        for (String value : metaFieldValueDTO.getValues()){
            if(check.isPresent() && check.get().contains(value)){
                throw new InvalidException(messageSource.getMessage("api.error.IdNotFound",null,Locale.ENGLISH));
            }

            newValues = newValues.concat(value + ",");
        }
        fieldValue.setValue(newValues);

        categoryMetadataFieldValueRepository.save(fieldValue);

        MetaFieldValueResponseDto metaFieldValueResponseDTO = new  MetaFieldValueResponseDto();
        metaFieldValueResponseDTO.setCategoryId(category.getId());
        metaFieldValueResponseDTO.setMetaFieldId(metaField.getId());
        metaFieldValueResponseDTO.setValues(fieldValue.getValue());

        return metaFieldValueResponseDTO;

    }

    public List<CategoryResponseDto> fetchCategories(Pageable paging) {
        Page<Category> categoryPage = categoryRepo.findAll(paging);
        List<CategoryResponseDto> responseCategoriesList = new ArrayList<>();
        for (Category category : categoryPage) {


            CategoryResponseDto categoryResponseDto = new CategoryResponseDto();

            categoryResponseDto.setId(category.getId());
            categoryResponseDto.setName(category.getName());
            categoryResponseDto.setParent(category.getParent());

            Set<ChildCategoryDto> categoryChildList = new HashSet<>();

            for(Category child: category.getChildren()){
                ChildCategoryDto childCategoryDTO = new ChildCategoryDto();
                childCategoryDTO.setId(child.getId());
                childCategoryDTO.setName(child.getName());
                categoryChildList.add(childCategoryDTO);

            }
            categoryResponseDto.setChildren(categoryChildList);

            List<CategoryMetadataFieldValue> metadataList =
                    categoryMetadataFieldValueRepository.findByCategory(category);

            List<MetadataResponseDTO> metaList = new ArrayList<>();
            for (CategoryMetadataFieldValue metadata: metadataList){
                MetadataResponseDTO metadataResponseDTO = new MetadataResponseDTO();
                metadataResponseDTO.setMetadataId(metadata.getCategoryMetadataField().getId());
                metadataResponseDTO.setFieldName(metadata.getCategoryMetadataField().getName());
                metadataResponseDTO.setPossibleValues(metadata.getValue());
                metaList.add(metadataResponseDTO);
            }
            categoryResponseDto.setMetadataList(metaList);

            responseCategoriesList.add(categoryResponseDto);
        }
        return responseCategoriesList;
    }
    public List<SellerCategoryResponseDTO> fetchSellerCategory(){
   // get Seller Category List
        List<Category> categoryList = categoryRepo.findAll();
      // list to save categories
        List<SellerCategoryResponseDTO> responseList = new ArrayList<>();
         // fetch category from categoryList
        for(Category category: categoryList){
            if(category.getChildren().isEmpty()){

                List<CategoryMetadataFieldValue> categoryMetadataList =
                        categoryMetadataFieldValueRepository.findByCategory(category);

                SellerCategoryResponseDTO sellerResponse = new SellerCategoryResponseDTO();

                sellerResponse.setId(category.getId());
                sellerResponse.setName(category.getName());
                sellerResponse.setParent(category.getParent());

                List<MetadataResponseDTO> metaList = new ArrayList<>();

                for (CategoryMetadataFieldValue metadata: categoryMetadataList){
                    MetadataResponseDTO metadataResponseDTO = new MetadataResponseDTO();
                    metadataResponseDTO.setMetadataId(metadata.getCategoryMetadataField().getId());
                    metadataResponseDTO.setFieldName(metadata.getCategoryMetadataField().getName());
                    metadataResponseDTO.setPossibleValues(metadata.getValue());
                    metaList.add(metadataResponseDTO);
                }
                sellerResponse.setMetadata(metaList);
                responseList.add(sellerResponse);
            }
        }
        return responseList;
    }

    public Set<Category> fetchCustomerCategory(Optional<Integer> Id){
        if(Id.isPresent()){

            Category category = categoryRepo.findById((long)Id.get()).orElseThrow(() -> new InvalidException(
                    messageSource.getMessage("api.error.IdNotFound", null, Locale.ENGLISH)));
            Set<Category> categoryChildList = category.getChildren();
            return categoryChildList;
        }
        else{
           // get category list
            List<Category> categoryList = categoryRepo.findAll();
            Set<Category> rootCategories = new HashSet<>();
            for(Category category: categoryList){
                if(category.getParent()==null){
                    rootCategories.add(category);
                }
            }
            return rootCategories;
        }
    }
}