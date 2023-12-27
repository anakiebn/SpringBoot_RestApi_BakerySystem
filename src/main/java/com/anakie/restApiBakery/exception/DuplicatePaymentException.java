package com.anakie.restApiBakery.exception;

public class DuplicatePaymentException extends Exception{

    public DuplicatePaymentException(String message){
        super(message);
    }
}
