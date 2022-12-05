package com.Ecomm.Ecommerce.Dto.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductVariationResponseDTO {
    private  Long id;
    private Long productId;

    private Object metadata;

    //    private MultipartFile image;
    private long quantity;

    private double price;
}