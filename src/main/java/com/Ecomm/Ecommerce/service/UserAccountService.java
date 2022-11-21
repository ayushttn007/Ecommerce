package com.Ecomm.Ecommerce.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserAccountService {

    public ResponseEntity<String> userLogout(HttpServletRequest request);

//    public ResponseEntity<String> ForgotPassword();
     public String userLogin(String userEmail, String userPassword);

    public  String userForgotPassword(String userEmail);
}
