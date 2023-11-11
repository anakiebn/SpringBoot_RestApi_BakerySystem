package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.*;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.repository.AccountStatusHistoryRepository;
import com.anakie.restApiBakery.repository.AddressRepository;
import com.anakie.restApiBakery.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public User save(User tempUser) throws DuplicateEmailException {
        String email = tempUser.getEmail();
        if (userRepository.findAll().stream().anyMatch((eachUser) -> eachUser.getEmail().equalsIgnoreCase(email))) {
            throw new DuplicateEmailException("Duplicate email: " + tempUser.getEmail() + " , Already exists, log in  or use another email");
        }
        // when ever we create a new user, the system automatically creates a new account for them
        saveUserAccount(userRepository.save(tempUser));
        return tempUser;

    }

    private void saveUserAccount(User user) {

        Account account = new Account();
        account.setAmount(0); // by default, they will have R 0.00 balance
        account.setUser(user);
        AccountStatusHistory accountStatusHistory = new AccountStatusHistory();
        accountStatusHistory.setStatus(Status.NEW); // new status of this account
        accountStatusHistory.setDateTime(LocalDateTime.now());
        account.getAccountStatusHistories().add(accountStatusHistory);
        accountStatusHistory.setAccount(accountService.save(account));
        accountStatusHistoryRepository.save(accountStatusHistory);

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
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("Failed to delete, user " + user.getId() + " not found!");
        }
        return userRepository.save(user);
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
    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }
}
