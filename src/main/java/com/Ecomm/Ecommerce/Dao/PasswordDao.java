package com.Ecomm.Ecommerce.Dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDao {
    private String oldPassword;
    private String password;
    private String confirmPassword;
}