package com.Ecomm.Ecommerce.dto;

import com.Ecomm.Ecommerce.entities.User;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Customer} entity
 */
@Data
public class CustomerDto extends UserDto {
    private long contact;
}