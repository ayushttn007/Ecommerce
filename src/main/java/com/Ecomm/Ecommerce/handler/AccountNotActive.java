package com.Ecomm.Ecommerce.handler;

public class AccountNotActive  extends  RuntimeException{
    public AccountNotActive(String errorMessage) {

        super(errorMessage);
    }
}
