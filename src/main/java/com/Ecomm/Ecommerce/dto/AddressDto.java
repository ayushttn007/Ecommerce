package com.Ecomm.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class AddressDto {

    @NotEmpty(message = "Field is mandatory.")
    @Pattern(regexp="(^[A-Za-z]*$).{2,30}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-30 characters.")
    private String country;

    @NotEmpty(message = "Field is mandatory.")
    @Pattern(regexp="(^[A-Za-z]*$).{2,30}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-30 characters.")
    private String city;

    @NotEmpty(message = "Field is mandatory.")
    @Pattern(regexp="(^[A-Za-z]*$).{2,30}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-30 characters.")
    private String state;

    @NotEmpty(message = "Field is mandatory.")
    @Pattern(regexp="(^[A-Za-z]*$).{2,50}",message = "Invalid Input. " +
            "Can only contain alphabets. Must contain be between 2-50 characters.")
    private String addressLine;

    @NotEmpty(message = "Field is mandatory.")
    @Pattern(regexp = "^\\d{6}$", message = "Enter a valid six-digit pincode.")
    private long zipCode;
    private String label;
}
