package com.Ecomm.Ecommerce.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class LoginDto {

    private String email;

    private String password;
}
