package com.Ecomm.Ecommerce.controller;

import com.Ecomm.Ecommerce.Dto.CategoryDto;
import com.Ecomm.Ecommerce.Dto.CategoryMetaFieldDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.CategoryViewDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.CategoryUpdateDto;
import com.Ecomm.Ecommerce.entities.CategoryMetadataField;
import com.Ecomm.Ecommerce.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    public ResponseEntity<CategoryViewDto> getAllCategory(@PathVariable long id){
        CategoryViewDto category = categoryService.fetchCategory(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
