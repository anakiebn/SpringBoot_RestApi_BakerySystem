package com.anakie.restApiBakery.service;


import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    User save(User user) throws DuplicateEmailException;
    User getUserById(Long id) throws UserNotFoundException;

    void deleteUserById(Long id) throws UserNotFoundException;

    User update(User user) throws UserNotFoundException;
    List<User> getAll();
    User saveAddress(Address address, Long id) throws UserNotFoundException, Exception;

    boolean existsById(Long id);
}
