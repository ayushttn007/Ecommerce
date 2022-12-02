package com.Ecomm.Ecommerce.Dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductDto {
    private String description="";
    private String name;
    private String brand;
    private Long categoryId;
}