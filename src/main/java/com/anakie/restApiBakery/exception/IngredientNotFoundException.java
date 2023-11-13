package com.anakie.restApiBakery.exception;


public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(String msg){
        super(msg);
    }
}
