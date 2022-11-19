package com.Ecomm.Ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.User} entity
 */
@Getter
@Setter
public class UserDto implements Serializable {


    @NotEmpty(message = "First Name is mandatory.")
    @Size(min = 2, max = 30, message = "Must contain 2-15 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "First Name can only contain alphabets.")
    private  String firstName;

    @Size(min = 2, max = 30, message = "Must contain 2-15 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Last Name can only contain alphabets.")
    private  String middleName;

    @NotEmpty(message = "Last Name is mandatory.")
    @Size(min = 2, max = 30, message = "Must contain 2-15 characters.")
    @Pattern(regexp="(^[A-Za-z]*$)",message = "Invalid Input. " +
            "Last Name can only contain alphabets.")
    private  String lastName;


    @Email(message = "Enter a valid email.")
    private  String email;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}",
            message = "Enter a valid password. Password must contain 8-15 characters " +
                    "with at least 1 lower case, 1 upper case, 1 special character, and 1 Number.")
    private  String password;

    private String confirmPassword;



}