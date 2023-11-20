package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;

public interface RegisterService {

    public void sendCode();
    public boolean codesMatch(String providedCode);
    public User register(String providedCode) throws DuplicateEmailException;
    public void setUser(User user);


}
