package com.Ecomm.Ecommerce.Dao;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AddressDao {

    @Pattern(regexp="(^[A-Za-z]*$).{2,30}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-30 characters.")
    private String country;

    @Pattern(regexp="(^[A-Za-z]*$).{2,30}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-30 characters.")
    private String city;

    @Pattern(regexp="(^[A-Za-z]*$).{2,30}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-30 characters.")
    private String state;

    @Pattern(regexp="(^[A-Za-z]*$).{2,50}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-50 characters.")
    private String addressLine;

    @Pattern(regexp = "^\\d{6}$", message = "Enter a valid six-digit pincode.")
    private String zipCode;

    private String label;
}
