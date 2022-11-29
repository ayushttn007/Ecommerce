package com.Ecomm.Ecommerce.Dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Customer} entity
 */
@Getter
@Setter
public class CustomerDto extends UserDto {

    @NotEmpty(message = "Contact number is mandatory field.")
    @Pattern(regexp = "^\\d{10}$", message = "Enter a valid ten-digit phone number.")
    private String contact;
}