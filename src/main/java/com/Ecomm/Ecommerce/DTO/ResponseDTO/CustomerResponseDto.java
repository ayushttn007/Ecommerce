package com.Ecomm.Ecommerce.DTO.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CustomerResponseDto {
    private long userid;
    private String fullName;
    private String email;
    private boolean is_active;



}
