package com.Ecomm.Ecommerce.Dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class EmailDto {

    @Email(message = "Enter a valid email.")
    public String email;
}
