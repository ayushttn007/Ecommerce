package com.Ecomm.Ecommerce.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Seller} entity
 */
@Data
public class SellerDto extends UserDto {
    private  String gst;
    private  long companyContact;
    private  String companyName;
}