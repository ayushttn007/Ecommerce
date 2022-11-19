package com.Ecomm.Ecommerce.handler;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String errorMessage){
        super(errorMessage);
    }
}
