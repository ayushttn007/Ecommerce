package com.Ecomm.Ecommerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.User} entity
 */
@Getter
@Setter
public class UserDto implements Serializable {

    private  String email;
    private  String firstName;
    private  String middleName;
    private  String lastName;
    private  String password;
    private String confirmPassword;



}