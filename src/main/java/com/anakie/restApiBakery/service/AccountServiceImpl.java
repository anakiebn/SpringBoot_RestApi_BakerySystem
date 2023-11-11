package com.anakie.restApiBakery.service;

import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.entity.AccountStatusHistory;
import com.anakie.restApiBakery.entity.Status;
import com.anakie.restApiBakery.exception.AccountNotFoundException;
import com.anakie.restApiBakery.exception.InsufficientFundsException;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
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
    public Account findByUserId(Long userId) throws AccountNotFoundException{
        return accountRepository.findByUserId(userId).orElseThrow(()->
                new AccountNotFoundException("Account for user= "+userId+" not found!"));
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

       if(!accountRepository.existsById(account.getId())){
           throw new AccountNotFoundException("Account not found, use existing account!");
        }
        return accountRepository.save(account);
    }

    @Override
    public double fundAccount(Long accountId, double amount) throws AccountNotFoundException {
        Account account=accountRepository.findById(accountId).orElseThrow(
                ()->new AccountNotFoundException("Account not found, use existing account!"));
        // if it's not negative
        if(amount>0){
            AccountStatusHistory accountStatusHistory=new AccountStatusHistory();
            // fund account
            account.setAmount(account.getAmount()+amount);
            accountStatusHistory.setDateTime(LocalDateTime.now());
            // update status
            accountStatusHistory.setStatus(Status.FUNDED);
            // we add the account status
            account.getAccountStatusHistories().add(accountStatusHistory);
            accountRepository.save(account);
        }
        // return the accounts balance
        return account.getAmount();
    }


    @Override
    public double useFunds(Long accountId, double amount) throws AccountNotFoundException, InsufficientFundsException {
        Account account=accountRepository.findById(accountId).orElseThrow(
                ()->new AccountNotFoundException("Account not found, use existing account!"));

        // check if we have enough funds
        if(amount>account.getAmount()){
            throw new InsufficientFundsException("Insufficient funds! Fund account");
        }
        // we make sure that it's not negative amount
        if(amount>0){
            AccountStatusHistory accountStatusHistory=new AccountStatusHistory();
            // use account funds
            account.setAmount(account.getAmount()-amount);
            accountStatusHistory.setDateTime(LocalDateTime.now());
            // update status
            accountStatusHistory.setStatus(Status.PAID_WITH_ACCOUNT);
            // we add the account status
            account.getAccountStatusHistories().add(accountStatusHistory);
            accountRepository.save(account);
        }
        //return the accounts balance
        return account.getAmount();

    }
}
