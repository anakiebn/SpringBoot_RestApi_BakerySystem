package com.anakie.restApiBakery.exception;

public class RecipeIngredientNotFoundException extends RuntimeException {
    public RecipeIngredientNotFoundException(String msg) {
        super(msg);
    }
}
