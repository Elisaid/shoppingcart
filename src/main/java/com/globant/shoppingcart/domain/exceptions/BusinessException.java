package com.globant.shoppingcart.domain.exceptions;


public class BusinessException extends RuntimeException {

    public BusinessException(String message){
        super(message);
    }
}
