package com.Ecomm.Ecommerce.service;

import com.Ecomm.Ecommerce.Dto.ProductDto;
import com.Ecomm.Ecommerce.Dto.ResponseDto.ProductViewDto;
import com.Ecomm.Ecommerce.Dto.UpdateDto.ProductUpdateDto;

import java.util.List;

public interface ProductService {

    String addProduct(String sellerEmail, ProductDto productDto);
    public ProductViewDto fetchProduct(String email, Long prodId);
    String removeProduct(String sellerEmail, Long productId);

    List<ProductViewDto> fetchAllProducts(String sellerEmail);

    String updateProduct(String name, Long prodId, ProductUpdateDto newProduct);

    String activateProduct(Long productId);

    String deActivateProduct(Long productId);

}
