package com.anakie.restApiBakery.exception;

public class InvalidPaymentMethod extends RuntimeException {
    public InvalidPaymentMethod(String msg) {
        super(msg);
    }
}
