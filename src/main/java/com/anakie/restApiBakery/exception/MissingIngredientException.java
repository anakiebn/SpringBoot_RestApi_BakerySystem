package com.anakie.restApiBakery.exception;

public class MissingIngredientException extends RuntimeException{
    public MissingIngredientException(String msg){
        super(msg);
    }
}
