package com.anakie.restApiBakery.exception;

import java.sql.SQLIntegrityConstraintViolationException;

public class DuplicateEmailException extends SQLIntegrityConstraintViolationException {
    public DuplicateEmailException(String msg){
        super(msg);
    }
}
