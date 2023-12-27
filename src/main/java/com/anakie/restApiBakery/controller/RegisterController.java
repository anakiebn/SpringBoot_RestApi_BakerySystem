package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
public class RegisterController {


    @Autowired
    private RegisterService registerService;

    @PostMapping
    public ResponseEntity<String> sendEmail(@RequestBody User user) throws DuplicateEmailException {
        registerService.setUser(user);
        registerService.sendCode();
        return new ResponseEntity<>("Email successfully sent to "+user.getEmail(), HttpStatus.CREATED);
    }

    @PostMapping("/{providedCode}")
    public ResponseEntity<User> register(@PathVariable String providedCode) throws DuplicateEmailException {
        return new ResponseEntity<>(registerService.register(providedCode), HttpStatus.CREATED);
    }


}
