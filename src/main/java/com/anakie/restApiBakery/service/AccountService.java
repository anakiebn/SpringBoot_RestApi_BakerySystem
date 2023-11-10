package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.exception.AccountNotFoundException;

import java.util.List;

public interface AccountService {

    Account save(Account account);
    Account findById(Long id) throws AccountNotFoundException;
    List<Account> findAll();
    void deleteById(Long id) throws AccountNotFoundException;

    Account update(Account account) throws AccountNotFoundException;
}
