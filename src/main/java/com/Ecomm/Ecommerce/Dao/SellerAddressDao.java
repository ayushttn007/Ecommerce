package com.Ecomm.Ecommerce.Dao;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class SellerAddressDao {

    private String country;
    private String city;
    private String state;
    private String addressLine;
    private String zipCode;
    private String label;
}
