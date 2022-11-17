package com.Ecomm.Ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String country;
    private String city;
    private String state;
    private String addressLine;
    private long zipCode;
    private String label;
}
