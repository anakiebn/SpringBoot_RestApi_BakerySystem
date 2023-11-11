package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.exception.AccountNotFoundException;
import com.anakie.restApiBakery.exception.InsufficientFundsException;
import com.anakie.restApiBakery.exception.UserNotFoundException;

import java.util.List;

public interface AccountService {

    Account save(Account account);
    Account findById(Long id) throws AccountNotFoundException;
    Account findByUserId(Long userId) throws AccountNotFoundException, UserNotFoundException;

    List<Account> findAll();
    void deleteById(Long id) throws AccountNotFoundException;

    Account update(Account account) throws AccountNotFoundException;

    double fundAccount(Long accountId, double amount) throws AccountNotFoundException;
    double useFunds(Long accountId, double amount) throws AccountNotFoundException, InsufficientFundsException;

}
