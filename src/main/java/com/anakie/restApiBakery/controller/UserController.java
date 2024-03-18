package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/find/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) throws UserNotFoundException {
            return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/activeUsers")
    public ResponseEntity<List<User>> findAllActiveUsers() {
        return new ResponseEntity<>(userService.findAllActiveUsers(), HttpStatus.OK);
    }

    @GetMapping("/noneActiveUsers")
    public ResponseEntity<List<User>> findAllNoneActiveUsers() {
        return new ResponseEntity<>(userService.findAllNoneActiveUsers(), HttpStatus.OK);
    }


    @PostMapping("/save")
    public ResponseEntity<User> save(@RequestBody User user) throws DuplicateEmailException {
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestBody User user) throws DuplicateEmailException {
        return new ResponseEntity<>(userService.update(user), HttpStatus.CREATED);
    }

    @PostMapping("/address/id")
    public ResponseEntity<User> saveAddress(@RequestBody Address address, @PathVariable Long id) throws Exception {
            return new ResponseEntity<>(userService.saveAddress(address, id), HttpStatus.CREATED);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws UserNotFoundException {

            userService.deleteById(id);
            return new ResponseEntity<>("User "+id+" successfully deleted", HttpStatus.GONE);
    }

}
