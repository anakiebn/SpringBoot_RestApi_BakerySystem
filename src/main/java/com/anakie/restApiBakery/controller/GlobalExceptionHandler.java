package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.exception.*;
import jakarta.persistence.PersistenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler({DuplicateEmailException.class,InsufficientFundsException.class,OutOfStockException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> notAcceptable(Exception  ex){
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Error occurred, ", ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> unknownError(Exception  ex){
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error occurred, ", ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);    }

    @ExceptionHandler({CartEmptyException.class, MissingIngredientException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Object> theNotFounds(Exception ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NO_CONTENT.value(), "Error occurred, ", ex.getMessage()), HttpStatus.NO_CONTENT);
    }

    @ExceptionHandler({PersistenceException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> dbExceptions(PersistenceException ex) {
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Error occurred, ", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<Object> validation(MethodArgumentNotValidException ex) {
        StringBuilder stringBuilder=new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error->stringBuilder.append(error.getField()).append(" ; "));

        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_ACCEPTABLE.value(), "Error occurred, ",stringBuilder.toString()), HttpStatus.FORBIDDEN);
    }





}

