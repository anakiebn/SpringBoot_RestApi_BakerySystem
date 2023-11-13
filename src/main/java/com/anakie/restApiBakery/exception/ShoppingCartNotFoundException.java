package com.anakie.restApiBakery.exception;

public class ShoppingCartNotFoundException extends RuntimeException {
    public ShoppingCartNotFoundException(String msg) {
        super(msg);
    }
}
