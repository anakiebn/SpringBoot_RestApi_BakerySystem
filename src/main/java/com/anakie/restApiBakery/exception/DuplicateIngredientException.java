package com.anakie.restApiBakery.exception;

public class DuplicateIngredientException extends RuntimeException {
    public DuplicateIngredientException(String s) {
        super(s);
    }
}
