package com.Ecomm.Ecommerce.service;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface UserAccountService {

    public ResponseEntity<String> userLogout(HttpServletRequest request);

//    public ResponseEntity<String> ForgotPassword();
}
