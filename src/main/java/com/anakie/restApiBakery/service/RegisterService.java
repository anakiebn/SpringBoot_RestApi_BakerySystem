package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;

public interface RegisterService {

    void sendCode() throws DuplicateEmailException;
    boolean codesMatch(String providedCode);
    User register(String providedCode) throws DuplicateEmailException;
    void setUser(User user);


}
