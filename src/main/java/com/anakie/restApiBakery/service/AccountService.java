package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.entity.Status;
import com.anakie.restApiBakery.exception.AccountNotFoundException;
import com.anakie.restApiBakery.exception.InsufficientFundsException;
import com.anakie.restApiBakery.exception.UserNotFoundException;

import java.util.List;

public interface AccountService {

    Account save(Account account);
    Account findById(Long id) throws AccountNotFoundException;
    Account findByUserId(Long userId) throws AccountNotFoundException, UserNotFoundException;

    List<Account> findAll();

    void changeAccountStatus(Account account, Status status);

    void deleteById(Long id) throws AccountNotFoundException;

    Account update(Account account) throws AccountNotFoundException;

    void fundAccount(Account account, double amount,Status status) throws AccountNotFoundException;

    // when a user decides to fund the account directly/ or save change
    void useFunds(Account account, double amount, Status status, int operation) throws Exception;

}
