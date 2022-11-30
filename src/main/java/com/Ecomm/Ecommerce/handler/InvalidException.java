package com.Ecomm.Ecommerce.handler;

public class InvalidException extends RuntimeException{

    public InvalidException(String errorMessage){
        super(errorMessage);
    }
}
