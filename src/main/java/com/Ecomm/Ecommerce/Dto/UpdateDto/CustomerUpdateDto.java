package com.Ecomm.Ecommerce.Dto.UpdateDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class CustomerUpdateDto {
    @Size(min = 2, max = 30, message = "Must contain 2-30 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Name can only contain alphabets.")
    private String firstName;

    @Size(min = 2, max = 30, message = "Must contain 2-30 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Name can only contain alphabets.")
    private String middleName;

    @Size(min = 2, max = 30, message = "Must contain 2-30 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Name can only contain alphabets.")
    private String lastName;


    @Size(min=10,max=10,message = "Enter a valid phone number.")
    private  String contact;
}
