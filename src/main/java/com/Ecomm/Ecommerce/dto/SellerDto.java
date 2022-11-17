package com.Ecomm.Ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Seller} entity
 */
@Getter
@Setter
public class SellerDto extends UserDto {
    private  String gst;
    private  long companyContact;
    private  String companyName;

    private  AddressDto address;
}