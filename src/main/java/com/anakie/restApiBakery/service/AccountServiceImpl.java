package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.entity.AccountStatusHistory;
import com.anakie.restApiBakery.entity.Status;
import com.anakie.restApiBakery.exception.AccountNotFoundException;
import com.anakie.restApiBakery.exception.InsufficientFundsException;
import com.anakie.restApiBakery.exception.NegativeAmountException;
import com.anakie.restApiBakery.repository.AccountRepository;
import com.anakie.restApiBakery.repository.AccountStatusHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;

    private final AccountStatusHistoryRepository accountStatusHistoryRepository;

    public AccountServiceImpl(AccountRepository accountRepository, AccountStatusHistoryRepository accountStatusHistoryRepository) {
        this.accountRepository = accountRepository;
        this.accountStatusHistoryRepository = accountStatusHistoryRepository;
    }

    @Override
    public Account save(Account account) {
        if (account == null) {
            throw new NullPointerException("Null account not allowed, provide none-null object");
        }
        return accountRepository.save(account);
    }

    @Override
    public Account findById(Long id) throws AccountNotFoundException {
        return accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account id= " + id + " not found"));
    }

    @Override
    public Account findByUserId(Long userId) throws AccountNotFoundException {
        return accountRepository.findByUserId(userId).orElseThrow(() ->
                new AccountNotFoundException("Account for user= " + userId + " not found!"));
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void changeAccountStatus(Account account, Status status) {
        if (!accountRepository.existsById(account.getId())) {
            throw new AccountNotFoundException("Can't change status, account not found, provide valid account");
        }
        AccountStatusHistory accountStatusHistory = new AccountStatusHistory();
        accountStatusHistory.setAccount(account);
        accountStatusHistory.setDateTime(LocalDateTime.now());
        accountStatusHistory.setStatus(status);

        accountStatusHistoryRepository.save(accountStatusHistory);

    }

    @Override
    public void deleteById(Long id) throws AccountNotFoundException {
        accountRepository.deleteById(id);
    }

    @Override
    public Account update(Account account) throws AccountNotFoundException {

        if (!accountRepository.existsById(account.getId())) {
            throw new AccountNotFoundException("Account not found, use existing account!");
        }
        return accountRepository.save(account);
    }


    // when a user decides to fund the account directly/ or save some change
    @Override
    public void fundAccount(Account account, double amount, Status status) throws AccountNotFoundException {
        // if the amount is negative
        if (amount < 0) {
            throw new NegativeAmountException("Negative deposit not allowed");
        }
        account.setAmount(account.getAmount() + amount); // fund account
        accountRepository.save(account); // update account

        AccountStatusHistory accountStatusHistory = new AccountStatusHistory();  // update status
        accountStatusHistory.setDateTime(LocalDateTime.now());
        accountStatusHistory.setStatus(status);
        accountStatusHistory.setAccount(account); // link the status to account
        accountStatusHistoryRepository.save(accountStatusHistory);

    }


    // when a user pays with account
    @Override
    public void useFunds(Account account, double amount,Status status,int operation) throws Exception {

        // if the amount is negative
        if (amount < 0) {
            throw new NegativeAmountException("Negative payment not allowed");
        }
        // operation 1= means we're paying with a card and account
        // operation 0= means we're paying with an account only
        // check if we have enough funds
        if (operation==0 && amount > account.getAmount()) {
            throw new InsufficientFundsException("Insufficient funds! Fund account");
        }else if(operation==1){
            account.setAmount(account.getAmount() - amount); // use account funds
            accountRepository.save(account); // update account

            AccountStatusHistory accountStatusHistory = new AccountStatusHistory();
            accountStatusHistory.setDateTime(LocalDateTime.now());
            accountStatusHistory.setAccount(account);
            accountStatusHistory.setStatus(status);
            accountStatusHistoryRepository.save(accountStatusHistory); // update account status
        }else{
            throw new Exception("Code error occured, We only have 2 operations numbered 0 and 1 only");
        }




    }


}
