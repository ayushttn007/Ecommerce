package com.Ecomm.Ecommerce.Dto.UpdateDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SellerUpdateDto {

    @Size(min = 2, max = 30, message = "Must contain 2-30 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Name can only contain alphabets.")
    private String firstName;


    @Size(min = 2, max = 30, message = "Must contain 2-30 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Name can only contain alphabets. Must contain 2-30 characters.")
    private String middleName;


    @Size(min = 2, max = 30, message = "Must contain 2-30 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Name can only contain alphabets. Must contain 2-30 characters.")
    private String lastName;

    @Size(min=10,max=10,message = "Enter a valid phone number.")
    private  String companyContact;

    @Size(max=30, message = "Enter a valid company name.")
    private  String companyName;

    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$",
            message = "Enter a valid GST number")
    private String gst;

}
