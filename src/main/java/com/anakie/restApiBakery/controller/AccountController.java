package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.Account;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.AccountNotFoundException;
import com.anakie.restApiBakery.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    // CRUD
    @GetMapping("/id")
    public ResponseEntity<Account> findById(@PathVariable("id") Long id) throws AccountNotFoundException {
            return new ResponseEntity<>(accountService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Account>> findAll() {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<Account> save(@RequestBody Account account) {
            return new ResponseEntity<>(accountService.save(account), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Account> update(@RequestBody Account account) {
        return new ResponseEntity<>(accountService.update(account), HttpStatus.CREATED);
    }
    @DeleteMapping("/id")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws AccountNotFoundException {
            accountService.deleteById(id);
            return new ResponseEntity<>("Account "+id+" successfully deleted", HttpStatus.GONE);
    }
}
