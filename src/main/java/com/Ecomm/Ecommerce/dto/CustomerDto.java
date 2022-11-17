package com.Ecomm.Ecommerce.dto;

import com.Ecomm.Ecommerce.entities.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Customer} entity
 */
@Getter
@Setter
public class CustomerDto extends UserDto {
    private long contact;
//    private  AddressDto address;
}