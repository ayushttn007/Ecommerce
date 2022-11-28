package com.Ecomm.Ecommerce.DTO.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomerProfileDto {
    private long userid;
    private String firstName;
    private String lastName;
    private String email;
    private boolean is_active;
    private  String contact;

}
