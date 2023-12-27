package com.anakie.restApiBakery.exception;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException(String negativePaymentNotAllowed) {
        super(negativePaymentNotAllowed);
    }
}
