package com.anakie.restApiBakery.exception;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String msg){
        super(msg);
    }
}
