package com.Ecomm.Ecommerce.controller;

import com.Ecomm.Ecommerce.Dto.ProductDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.ProductViewDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.ProductUpdateDto;
import com.Ecomm.Ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
@Autowired
    ProductService productService;

    @PostMapping("/seller/product/add")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> addProduct(Authentication authentication, @RequestBody ProductDto productDto){
        String userName = authentication.getName();
        String message=productService.addProduct(userName,productDto);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @GetMapping("/seller/product")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<ProductViewDto> fetchProduct(Authentication authentication, @RequestParam Long prodId){
        String userName = authentication.getName();
        ProductViewDto product=productService.fetchProduct(userName,prodId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @GetMapping("/seller/products")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<List<ProductViewDto>> fetchAllProducts(Authentication authentication){
        String userName = authentication.getName();
        List<ProductViewDto> productList=productService.fetchAllProducts(userName);
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @DeleteMapping("/seller/product/delete")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> deleteProduct(Authentication authentication,@RequestParam Long productId){
        String userName = authentication.getName();
        String message=productService.removeProduct(userName,productId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @PatchMapping("/seller/product/update")
    @PreAuthorize("hasAuthority('SELLER')")
    public ResponseEntity<String> updateProduct(Authentication authentication, @RequestParam Long productId, @RequestBody ProductUpdateDto product){
        String userName = authentication.getName();
        String message=productService.updateProduct(userName,productId,product);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @PatchMapping("admin/product/activate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> activateProduct(@RequestParam Long prodId){
        String message=productService.activateProduct(prodId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
    @PatchMapping("admin/product/de-activate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deactivateProduct(@RequestParam Long prodId){
        String message=productService.deActivateProduct(prodId);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

}
