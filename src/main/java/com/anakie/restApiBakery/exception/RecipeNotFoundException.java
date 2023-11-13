package com.anakie.restApiBakery.exception;

public class RecipeNotFoundException extends RuntimeException{
    public RecipeNotFoundException(String msg){
        super(msg);
    }
}
