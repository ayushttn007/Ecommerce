package com.Ecomm.Ecommerce.DTO;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


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