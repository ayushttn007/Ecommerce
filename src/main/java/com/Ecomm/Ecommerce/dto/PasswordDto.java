package com.Ecomm.Ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PasswordDto {

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}",
            message = "Enter a valid password. Password must contain 8-15 characters " +
                    "with at least 1 lower case, 1 upper case, 1 special character, and 1 Number.")
    private String password;

    @NotNull
    private String confirmPassword;
}
