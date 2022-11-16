package com.Ecomm.Ecommerce.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.Ecomm.Ecommerce.entities.User} entity
 */
@Data
public class UserDto implements Serializable {
    private  String email;
    private  String firstName;
    private  String middleName;
    private  String lastName;
    private  String password;

}