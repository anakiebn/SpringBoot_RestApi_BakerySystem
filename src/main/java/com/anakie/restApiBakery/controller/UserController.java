package com.anakie.restApiBakery.controller;

import com.anakie.restApiBakery.entity.Address;
import com.anakie.restApiBakery.entity.User;
import com.anakie.restApiBakery.exception.DuplicateEmailException;
import com.anakie.restApiBakery.exception.UserNotFoundException;
import com.anakie.restApiBakery.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);

        } catch (UserNotFoundException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception ex){
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user) {
        try {
            return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
        } catch (DuplicateEmailException ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/address/{id}")
    public ResponseEntity<User> saveAddress(@RequestBody Address address, @PathVariable Long id) {

        try {

            return new ResponseEntity<>(userService.saveAddress(address, id), HttpStatus.CREATED);
        } catch (UserNotFoundException ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>("User "+id+" successfully deleted", HttpStatus.GONE);
        } catch (UserNotFoundException ex) {
            log.error("Exception occurred: " + ex.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
