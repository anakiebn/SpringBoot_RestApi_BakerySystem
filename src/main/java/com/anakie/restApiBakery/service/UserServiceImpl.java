package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.repository.AccountStatusHistoryRepository;
import com.anakie.restApiBakery.repository.AddressRepository;
import com.anakie.restApiBakery.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private AccountStatusHistoryRepository accountStatusHistoryRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AccountService accountService;


    @Override
    public User save(User user) throws DuplicateEmailException {

        if(user==null){
            throw new NullPointerException("Null user not allowed, provide none-null object");
        }
        if (userRepository.findAll().stream().anyMatch((eachUser) -> eachUser.getEmail().equalsIgnoreCase(user.getEmail()))) {
            throw new DuplicateEmailException("Duplicate email: " + user.getEmail() + " , Already exists, log in  or use another email");
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt(10,new SecureRandom()))); // encrypt password
        return saveUserAccount(userRepository.save(user)); // when ever we create new users, the system automatically creates a new account for them
    }

    private User saveUserAccount(User user){
        user.setActive(true); // all new users must be set to active
        Account account = new Account();
        account.setAmount(0); // by default, they will have R 0.00 balance
        account.setUser(user);
        ; // link db account to the present account to get its id
        accountService.changeAccountStatus(account=accountService.save(account),Status.New);
        return user;
    }

    @Override
    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User " + id + " is not found"));
    }


    @Override
    public void deleteById(Long id) throws UserNotFoundException {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Failed to delete, user " + id + " not found!")));
    }

    @Override
    public User update(User user) throws UserNotFoundException {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
        }
        throw new UserNotFoundException("Failed to update, user " + user.getId() + " not found!");


    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User saveAddress(Address address, Long id) throws UserNotFoundException {

        User user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id " + id + " is not found")
        );
        user.getAddressList().add(addressRepository.save(address));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAllActiveUsers(){
        return userRepository.findAll().stream().filter(User::isActive).toList();
    }

    @Override
    public List<User> findAllNoneActiveUsers(){
        return userRepository.findAll().stream().filter(user->!user.isActive()).toList();
    }
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
