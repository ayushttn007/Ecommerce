package com.Ecomm.Ecommerce.handler;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
