package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.service.RegisterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/registration")
@RequiredArgsConstructor
public class RegisterController {


    @Autowired
    private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> sendEmail(@RequestBody User user) throws DuplicateEmailException {
        registerService.setUser(user);
        registerService.sendCode();
        return new ResponseEntity<>("Email successfully sent to "+user.getEmail(), HttpStatus.CREATED);
    }

    @PostMapping("/confirmRegistration/{providedCode}")
    public ResponseEntity<User> register(@PathVariable String providedCode) throws DuplicateEmailException {
        return new ResponseEntity<>(registerService.register(providedCode), HttpStatus.CREATED);
    }


}
