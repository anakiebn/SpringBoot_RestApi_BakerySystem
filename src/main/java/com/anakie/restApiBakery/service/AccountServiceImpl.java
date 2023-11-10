package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.exception.AccountNotFoundException;
import com.anakie.restApiBakery.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountServiceImpl implements AccountService{

    @Autowired
    private AccountRepository accountRepository;


    @Override
    public Account save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(()->new AccountNotFoundException("Account id= "+id+" not found"));
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteById(Long id) throws AccountNotFoundException {
        accountRepository.deleteById(id);
    }

    @Override
    public Account update(Account account) throws AccountNotFoundException {
        return null;
    }
}
