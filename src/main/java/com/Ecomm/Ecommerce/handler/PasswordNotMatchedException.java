package com.Ecomm.Ecommerce.handler;

public class PasswordNotMatchedException extends RuntimeException{
    public PasswordNotMatchedException(String errorMessage) {
        super(errorMessage);
    }
}
