package com.anakie.restApiBakery.exception;

public class PaymentFailedException extends RuntimeException {
    public PaymentFailedException(String s) {
        super(s);
    }
}
