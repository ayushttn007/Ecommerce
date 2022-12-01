package com.Ecomm.Ecommerce.controller;

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
import com.Ecomm.Ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class CategoryController {

    @Autowired
    CategoryService categoryService;


    @PostMapping(path = "/add/metadata_field")
//    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<String> addMetadataField(@RequestBody CategoryMetaFieldDto categoryMetaFieldDto){
        String responseMessage = categoryService.saveMetaValue(categoryMetaFieldDto);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping(path = "/metadata_field")
//    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<List<CategoryMetaFieldDto>> getMetadataField(){
        List<CategoryMetaFieldDto> responseList= categoryService.fetchMetafields();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PostMapping(path = "/add/category")
//    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto){
        String responseMessage = categoryService.saveCategory(categoryDto);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PutMapping(path = "/update/category")
//    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<String> updateCategory(@RequestParam long categoryId,@RequestBody CategoryUpdateDto categoryDto){
        String responseMessage = categoryService.updateCategory(categoryId,categoryDto);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping(path = "/category/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")

    public ResponseEntity<CategoryViewDto> getCategory(@PathVariable long id){
        CategoryViewDto category = categoryService.fetchCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }



    @PostMapping("/metadata/values")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<MetaFieldValueResponseDto> addMetaFieldValues(@RequestBody MetaFieldValueDto metaFieldValueDto){
        Long categoryId = metaFieldValueDto.getCategoryId();
        Long metaFieldId = metaFieldValueDto.getMetadataId();
        MetaFieldValueResponseDto response = categoryService.saveMetaFieldValues(metaFieldValueDto);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> fetchCategories(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @Pattern(regexp="DESC|ASC") @RequestParam(required = false) String sortOrder){
        if(sortOrder=="DESC"){
            Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
            List<CategoryResponseDto> fieldList = categoryService.fetchCategories(paging);
            return new ResponseEntity<>(fieldList,HttpStatus.OK);
        }
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        List<CategoryResponseDto> fieldList = categoryService.fetchCategories(paging);
        return new ResponseEntity<>(fieldList,HttpStatus.OK);
    }


    @GetMapping("/seller/category")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity< List<SellerCategoryResponseDTO> > fetchSellerCategory(){
        List<SellerCategoryResponseDTO> responseList = categoryService.fetchSellerCategory();
        return new ResponseEntity<>(responseList,HttpStatus.OK);
    }

    @GetMapping(value = {"/customer/category", "/customer/category/{id}"})
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<Set<Category>> fetchCustomerCategory(@PathVariable("id") Optional<Integer> optionalId){
        Set<Category> responseList = categoryService.fetchCustomerCategory(optionalId);
        return new ResponseEntity<>(responseList,HttpStatus.OK);
    }

}
