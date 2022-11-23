package com.Ecomm.Ecommerce.dto;

import com.Ecomm.Ecommerce.entities.User;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.Customer} entity
 */
@Getter
@Setter
public class CustomerDto extends UserDto {

    @NotNull(message = "Contact number is mandatory field.")
    @Size(min=10,max=10,message = "Enter a valid Contact Number.")
    private String contact;
}