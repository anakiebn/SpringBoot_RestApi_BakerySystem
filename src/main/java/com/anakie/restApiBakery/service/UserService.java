package com.anakie.restApiBakery.service;


import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;

import java.util.List;

public interface UserService {

    User save(User user) throws DuplicateEmailException;
    User findById(Long id) throws UserNotFoundException;

    void deleteById(Long id) throws UserNotFoundException;

    User update(User user) throws UserNotFoundException;
    List<User> findAll();
    User saveAddress(Address address, Long userId) throws UserNotFoundException, Exception;

    List<User> findAllActiveUsers();

    List<User> findAllNoneActiveUsers();

    boolean existsByEmail(String email);
    boolean existsById(Long id);
}
